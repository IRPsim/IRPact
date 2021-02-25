package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.commons.util.xlsx.XlsxUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.attribute.SpatialDoubleAttributeBase;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttributeBase;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileLoader implements SpatialInformationLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader.class);

    protected ResourceLoader loader;
    protected String inputFileName;
    protected List<List<SpatialAttribute<?>>> data;

    public SpatialTableFileLoader() {
    }

    public void setLoader(ResourceLoader loader) {
        this.loader = loader;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void initalize() throws MissingDataException {
        try {
            parse();
        } catch (IOException | InvalidFormatException e) {
            throw new MissingDataException(e);
        }
    }

    @Override
    public List<List<SpatialAttribute<?>>> getAllAttributes() {
        if(data == null) {
            throw new IllegalStateException("not initalized");
        }
        return data;
    }

    private void parse() throws IOException, InvalidFormatException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
        }

        String csvFile = inputFileName + ".csv";
        if(loader.hasPath(csvFile)) {
            Path csvPath = loader.get(csvFile);
            LOGGER.debug("load csv file '{}'", csvPath);
            data = parseCsv(csvPath);
            return;
        }
        if(loader.hasResource(csvFile)) {
            LOGGER.debug("load csv resource '{}'", csvFile);
            try(InputStream in = loader.getResourceAsStream(csvFile)) {
                data = parseCsv(in);
            }
        }

        String xlsxFile = inputFileName + ".xlsx";
        if(loader.hasPath(xlsxFile)) {
            Path xlsxPath = loader.get(xlsxFile);
            LOGGER.debug("load xlsx file '{}'", xlsxPath);
            try(InputStream in = Files.newInputStream(xlsxPath)) {
                data = parseXlsx(in);
            }
            return;
        }
        if(loader.hasResource(xlsxFile)) {
            LOGGER.debug("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getResourceAsStream(xlsxFile)) {
                data = parseXlsx(in);
            }
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    //=========================
    //csv
    //=========================

    private static List<List<SpatialAttribute<?>>> parseCsv(InputStream in) {
        throw new UnsupportedOperationException();
    }

    private static List<List<SpatialAttribute<?>>> parseCsv(Path path) {
        throw new UnsupportedOperationException();
    }

    //=========================
    //xlsx
    //=========================

    private static List<List<SpatialAttribute<?>>> parseXlsx(InputStream in) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        return parseXlsx(book);
    }

    private static List<List<SpatialAttribute<?>>> parseXlsx(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheetAt(0);
        List<List<SpatialAttribute<?>>> out = new ArrayList<>();
        parse(sheet, out);
        return out;
    }

    private static void parse(XSSFSheet sheet, List<List<SpatialAttribute<?>>> out) {
        Iterator<Row> rowIter = sheet.rowIterator();
        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("empty sheet");
        }
        rowIter.next(); //skip info row

        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("missing header");
        }
        Row headerRow = rowIter.next();
        String[] header = XlsxUtil.extractHeader(headerRow);

        while(rowIter.hasNext()) {
            Row dataRow = rowIter.next();
            List<SpatialAttribute<?>> rowList = extractRowData(dataRow, header, sheet.getSheetName());
            if(rowList == null) {
                //blank row -> end of data
                break;
            }
            out.add(rowList);
        }
    }

    private static List<SpatialAttribute<?>> extractRowData(Row row, String[] header, String sheetName) {
        List<SpatialAttribute<?>> out = new ArrayList<>(header.length);
        Iterator<Cell> cellIter = row.cellIterator();
        int index = 0;
        while(cellIter.hasNext() && index < header.length) {
            Cell cell = cellIter.next();
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue = cell.getNumericCellValue();

                    //TEST
                    CellStyle cs = cell.getCellStyle();
                    String format = cs.getDataFormatString();
                    if(!"General".equals(format)) {
                        DecimalFormat df = new DecimalFormat(format);
                        String formatted = df.format(numValue);
                        numValue = Double.parseDouble(formatted);
                    }
                    //===

                    out.add(new SpatialDoubleAttributeBase(header[index], numValue));
                    break;

                case STRING:
                    String strValue = cell.getStringCellValue();
                    out.add(new SpatialStringAttributeBase(header[index], strValue));
                    break;

                default:
                    if(cell.getCellType() == CellType.BLANK) {
                        if(index == 0) {
                            if(XlsxUtil.isBlankRow(cellIter, header.length)) {
                                return null;
                            } else {
                                throw new IllegalArgumentException("unsupported cell type: "
                                        + cell.getCellType()
                                        + " (" + sheetName + ", " + index + ")"
                                );
                            }
                        } else {
                            String blankValue = cell.getStringCellValue();
                            out.add(new SpatialStringAttributeBase(header[index], blankValue));
                        }
                    } else {
                        throw new IllegalArgumentException("unsupported cell type: "
                                + cell.getCellType()
                                + " (" + sheetName + ", " + index + ")"
                        );
                    }
                    break;
            }
            index++;
        }
        return out;
    }
}
