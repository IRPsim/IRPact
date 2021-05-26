package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.commons.util.xlsx.CellValueConverter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetParser;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileLoader implements SpatialInformationLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader.class);

    protected static final CellValueConverter<String, SpatialAttribute> STRING_CONVERTER =
            (columnIndex, header, value) -> new BasicSpatialStringAttribute(header[columnIndex], value);
    protected static final CellValueConverter<Number, SpatialAttribute> NUMERIC_CONVERTER =
            (columnIndex, header, value) -> new BasicSpatialDoubleAttribute(header[columnIndex], value.doubleValue());

    protected ResourceLoader loader;
    protected String inputFileName;
    protected Table<SpatialAttribute> data;

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
    public SpatialTableFileContent getAllAttributes() {
        if(data == null) {
            throw new IllegalStateException("not initalized");
        }
        return new SpatialTableFileContent(data);
    }

    private void parse() throws IOException, InvalidFormatException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
        }

        LOGGER.info("csv disabled");
//        String csvFile = inputFileName + ".csv";
//        if(loader.hasPath(csvFile)) {
//            Path csvPath = loader.get(csvFile);
//            LOGGER.trace("load csv file '{}'", csvPath);
//            data = parseCsv(csvPath);
//            return;
//        }
//        if(loader.hasResource(csvFile)) {
//            LOGGER.trace("load csv resource '{}'", csvFile);
//            try(InputStream in = loader.getResourceAsStream(csvFile)) {
//                data = parseCsv(in);
//            }
//            return;
//        }

        String xlsxFile = inputFileName + ".xlsx";
        if(loader.hasExternal(xlsxFile)) {
            Path xlsxPath = loader.getExternal(xlsxFile);
            LOGGER.trace("load xlsx file '{}'", xlsxPath);
            try(InputStream in = Files.newInputStream(xlsxPath)) {
                data = parseXlsx(in);
            }
            return;
        }
        if(loader.hasInternal(xlsxFile)) {
            LOGGER.trace("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getInternalAsStream(xlsxFile)) {
                data = parseXlsx(in);
            }
            return;
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    //=========================
    //csv
    //=========================

    private static Table<SpatialAttribute> parseCsv(InputStream in) {
        throw new UnsupportedOperationException();
    }

    private static Table<SpatialAttribute> parseCsv(Path path) {
        throw new UnsupportedOperationException();
    }

    //=========================
    //xlsx
    //=========================

    public static Table<SpatialAttribute> parseXlsx(InputStream in) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book);
    }

    public static Table<SpatialAttribute> parseXlsx(Path path) throws IOException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in);
        }
    }

    public static Table<SpatialAttribute> parseXlsx(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheetAt(0);

        XlsxSheetParser<SpatialAttribute> parser = new XlsxSheetParser<>();
        parser.setTextConverter(STRING_CONVERTER);
        parser.setNumbericConverter(NUMERIC_CONVERTER);
        parser.setNumberOfInfoRows(1);

        XlsxTable<SpatialAttribute> table = new XlsxTable<>();
        table.load(parser, sheet);

        return table;
    }
}
