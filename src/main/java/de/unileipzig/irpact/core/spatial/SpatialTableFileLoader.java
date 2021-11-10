package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Header;
import de.unileipzig.irpact.commons.util.table.SimpleHeader;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.commons.util.xlsx.CellValueConverter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetParser;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetWriter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileLoader implements SpatialInformationLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader.class);

    public static final CellValueConverter<String, SpatialAttribute> STR2ATTR =
            (header, columnIndex, value) -> new BasicSpatialStringAttribute(header.getLabel(columnIndex), value);
    public static final CellValueConverter<Number, SpatialAttribute> NUM2ATTR =
            (header, columnIndex, value) -> new BasicSpatialDoubleAttribute(header.getLabel(columnIndex), value.doubleValue());
    public static final CellValueConverter<Void, SpatialAttribute> EMPTY2ATTR =
            (header, columnIndex, value) -> null;

    public static final CellValueConverter<SpatialAttribute, String> ATTR2STR = new CellValueConverter<SpatialAttribute, String>() {

        @Override
        public boolean isSupported(Header header, int columnIndex, SpatialAttribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.STRING);
        }

        @Override
        public String convert(Header header, int columnIndex, SpatialAttribute value) {
            return value.asValueAttribute().getStringValue();
        }
    };
    public static final CellValueConverter<SpatialAttribute, Number> ATTR2NUM = new CellValueConverter<SpatialAttribute, Number>() {

        @Override
        public boolean isSupported(Header header, int columnIndex, SpatialAttribute value) {
            return value.isValueAttribute() && value.asValueAttribute().isDataType(DataType.DOUBLE);
        }

        @Override
        public Number convert(Header header, int columnIndex, SpatialAttribute value) {
            return value.asValueAttribute().getDoubleValue();
        }
    };

    protected ResourceLoader loader;
    protected String inputFileName;
    protected String sheetName;
    protected Table<SpatialAttribute> data;
    protected double coverage = Double.NaN;

    public SpatialTableFileLoader() {
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
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
        return new SpatialTableFileContent(inputFileName, data, coverage);
    }

    private void parse0() throws IOException, InvalidFormatException, ParsingException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
        }

        if(tryLoadXlsx()) {
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
                data = parseXlsx(in, sheetName);
            }
            return true;
        }
        if(loader.hasInternal(xlsxFile)) {
            LOGGER.trace("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getInternalAsStream(xlsxFile)) {
                data = parseXlsx(in, sheetName);
            }
            return true;
        }
        return false;
    }

    //=========================
    //xlsx
    //=========================

    public static Table<SpatialAttribute> parseXlsx(InputStream in) throws IOException, ParsingException, InvalidFormatException {
        return parseXlsx(in, null);
    }

    public static Table<SpatialAttribute> parseXlsx(InputStream in, String sheetName) throws IOException, ParsingException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book, sheetName);
    }

    public static Table<SpatialAttribute> parseXlsx(Path path) throws IOException, ParsingException, InvalidFormatException {
        return parseXlsx(path, null);
    }

    public static Table<SpatialAttribute> parseXlsx(Path path, String sheetName) throws IOException, ParsingException, InvalidFormatException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in, sheetName);
        }
    }

    public static Table<SpatialAttribute> parseXlsx(XSSFWorkbook book) throws ParsingException, IOException, InvalidFormatException {
        return parseXlsx(book, null);
    }

    public static Table<SpatialAttribute> parseXlsx(XSSFWorkbook book, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        XSSFSheet sheet = sheetName == null
                ? book.getSheetAt(0)
                : book.getSheet(sheetName);

        if(sheet == null) {
            if(sheetName != null) {
                throw new NoSuchElementException("missing sheet: " + sheetName);
            } else {
                throw new NoSuchElementException("missing sheet at index 0");
            }
        }

        XlsxSheetParser<SpatialAttribute> parser = new XlsxSheetParser<>();
        parser.setTextConverter(STR2ATTR);
        parser.setNumericConverter(NUM2ATTR);
        parser.setEmptyConverter(EMPTY2ATTR);
        parser.setNumberOfInfoRows(0);

        XlsxTable<SpatialAttribute> table = new XlsxTable<>();
        table.load(parser, sheet);

        return table;
    }

    public static void writeXlsx(Path target, String sheetName, String info, Table<SpatialAttribute> data) throws IOException {
        XlsxSheetWriter<SpatialAttribute> writer = new XlsxSheetWriter<>();
        writer.setNumericConverter(ATTR2NUM);
        writer.setTextConverter(ATTR2STR);
        Collection<String> infoColl = info == null
                ? Collections.emptyList()
                : Collections.singleton(info);
        writer.write(target, sheetName, infoColl, new SimpleHeader(data.getHeader()), data.listTable());
    }
}
