package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.spatial.attribute.AbstractSpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.io3.TableData3;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetParser3;
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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileLoader2 implements SpatialInformationLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader2.class);

    protected ResourceLoader loader;
    protected String inputFileName;
    protected String sheetName;
    protected int sheetIndex = -1;
    protected TableData3<SpatialAttribute> data;
    protected double coverage = Double.NaN;

    public SpatialTableFileLoader2() {
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    public void setLoader(ResourceLoader loader) {
        this.loader = loader;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void parse() throws MissingDataException {
        try {
            parse0();
        } catch (ParsingException | IOException | InvalidFormatException e) {
            throw new MissingDataException(e);
        }
    }

    @Override
    public SpatialTableFileContent getAllAttributes() {
        if(data == null) {
            throw new IllegalStateException("not initalized");
        }
        throw new UnsupportedOperationException("TODO");
        //return new SpatialTableFileContent2(inputFileName, data, coverage);
    }

    private void parse0() throws IOException, InvalidFormatException, ParsingException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
        }

        if(tryLoadXlsx()) {
            prepareData();
            return;
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    protected boolean tryLoadXlsx() throws IOException, ParsingException, InvalidFormatException {
        String xlsxFile = inputFileName + ".xlsx";

        if(loader.hasExternal(xlsxFile)) {
            Path xlsxPath = loader.getExternal(xlsxFile);
            LOGGER.trace("load xlsx file '{}'", xlsxPath);
            try(InputStream in = Files.newInputStream(xlsxPath)) {
                data = tryLoadXlsx(in);
            }
            return true;
        }

        if(loader.hasInternal(xlsxFile)) {
            LOGGER.trace("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getInternalAsStream(xlsxFile)) {
                data = tryLoadXlsx(in);
            }
            return true;
        }

        return false;
    }

    protected void prepareData() {
        LOGGER.trace("prepare data");
        List<SpatialAttribute> headerRow = data.deleteRow(0);
        for(int columnIndex = 0; columnIndex < headerRow.size(); columnIndex++) {
            String columnName = headerRow.get(columnIndex).asValueAttribute().getStringValue();
            for(int rowIndex = 0; rowIndex < data.getNumberOfRows(); rowIndex++) {
                SpatialAttribute attr = data.get(rowIndex, columnIndex);
                AbstractSpatialAttribute attr0 = (AbstractSpatialAttribute) attr;
                attr0.setName(columnName);
            }
        }
    }

    protected TableData3<SpatialAttribute> tryLoadXlsx(InputStream in) throws ParsingException, IOException, InvalidFormatException {
        if(sheetName == null) {
            if(sheetIndex < 0) {
                return parseXlsx(in);
            } else {
                return parseXlsx(in, sheetIndex);
            }
        } else {
            if(sheetIndex < 0) {
                return parseXlsx(in, sheetName);
            } else {
                throw new IllegalArgumentException(StringUtil.format("sheet name '{}' and index '{}' defined", sheetName, sheetIndex));
            }
        }
    }

    //=========================
    //xlsx
    //=========================

    public static TableData3<SpatialAttribute> parseXlsx(Path path) throws IOException, ParsingException, InvalidFormatException {
        return parseXlsx(path, 0);
    }

    public static TableData3<SpatialAttribute> parseXlsx(Path path, int sheetIndex) throws IOException, ParsingException, InvalidFormatException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in, sheetIndex);
        }
    }

    public static TableData3<SpatialAttribute> parseXlsx(Path path, String sheetName) throws IOException, ParsingException, InvalidFormatException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in, sheetName);
        }
    }

    public static TableData3<SpatialAttribute> parseXlsx(InputStream in) throws IOException, ParsingException, InvalidFormatException {
        return parseXlsx(in, 0);
    }

    public static TableData3<SpatialAttribute> parseXlsx(InputStream in, String sheetName) throws IOException, ParsingException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book, sheetName);
    }

    public static TableData3<SpatialAttribute> parseXlsx(InputStream in, int sheetIndex) throws IOException, ParsingException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book, sheetIndex);
    }

    public static TableData3<SpatialAttribute> parseXlsx(XSSFWorkbook book) throws ParsingException, IOException, InvalidFormatException {
        return parseXlsx(book, 0);
    }

    public static TableData3<SpatialAttribute> parseXlsx(XSSFWorkbook book, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        XSSFSheet sheet = book.getSheet(sheetName);

        if(sheet == null) {
            throw new NoSuchElementException("missing sheet: " + sheetName);
        }

        return parseXlsx(sheet);
    }

    public static TableData3<SpatialAttribute> parseXlsx(XSSFWorkbook book, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        XSSFSheet sheet = book.getSheetAt(sheetIndex);

        if(sheet == null) {
            throw new NoSuchElementException("missing sheet at index: " + sheetIndex);
        }

        return parseXlsx(sheet);
    }

    public static TableData3<SpatialAttribute> parseXlsx(XSSFSheet sheet) throws ParsingException, IOException, InvalidFormatException {
        XlsxSheetParser3<SpatialAttribute> parser = new XlsxSheetParser3<>();
        parser.setCellHandler(XlsxSheetParser3.forSpatial());
        return parser.parse(sheet);
    }
}
