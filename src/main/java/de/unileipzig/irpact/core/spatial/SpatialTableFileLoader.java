package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.csv.CsvParser;
import de.unileipzig.irpact.commons.util.csv.CsvValueConverter;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.util.table.Header;
import de.unileipzig.irpact.commons.util.table.SimpleHeader;
import de.unileipzig.irpact.commons.util.table.SimpleTable;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.commons.util.xlsx.CellValueConverter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetParser;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetWriter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static final CsvValueConverter<SpatialAttribute> CSV_STR2ATTR = (header, columnIndex, value) -> {
        String headerEntry = header.getLabel(columnIndex);
        switch (headerEntry) {
            case RAConstants.ID:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ADDRESS:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.ZIP:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER_STR:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE_COUNT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ORIENTATION:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SLOPE:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR_ADDR:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.DOM_MILIEU:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.AREA:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.X_CENT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.Y_CENT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            default:
                throw new IllegalArgumentException("unknown header: " + headerEntry);
        }
    };

    protected ResourceLoader loader;
    protected String inputFileName;
    protected Table<SpatialAttribute> data;
    protected double coverage = Double.NaN;
    protected boolean preferCsv = false;

    public SpatialTableFileLoader() {
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

    public void initalize() throws MissingDataException {
        try {
            parse();
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

    private void parse() throws IOException, InvalidFormatException, ParsingException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
        }

        if(preferCsv) {
            if(tryLoadCsv()) {
                return;
            }
            if(tryLoadXlsx()) {
                return;
            }
        } else {
            if(tryLoadXlsx()) {
                return;
            }
            if(tryLoadCsv()) {
                return;
            }
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    protected boolean tryLoadCsv() throws ParsingException, IOException, InvalidFormatException {
        LOGGER.info("csv disabled");
        if(true) return false;

        String csvFile = inputFileName + ".csv";
        if(loader.hasExternal(csvFile)) {
            Path csvPath = loader.getExternal(csvFile);
            LOGGER.trace("load xlsx file '{}'", csvPath);
            try(InputStream in = Files.newInputStream(csvPath)) {
                data = parseCsv(in);
            }
            return true;
        }
        if(loader.hasInternal(csvFile)) {
            LOGGER.trace("load xlsx resource '{}'", csvFile);
            try(InputStream in = loader.getInternalAsStream(csvFile)) {
                data = parseCsv(in);
            }
            return true;
        }
        return false;
    }

    protected boolean tryLoadXlsx() throws IOException, ParsingException, InvalidFormatException {
        String xlsxFile = inputFileName + ".xlsx";
        if(loader.hasExternal(xlsxFile)) {
            Path xlsxPath = loader.getExternal(xlsxFile);
            LOGGER.trace("load xlsx file '{}'", xlsxPath);
            try(InputStream in = Files.newInputStream(xlsxPath)) {
                data = parseXlsx(in);
            }
            return true;
        }
        if(loader.hasInternal(xlsxFile)) {
            LOGGER.trace("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getInternalAsStream(xlsxFile)) {
                data = parseXlsx(in);
            }
            return true;
        }
        return false;
    }

    //=========================
    //csv
    //=========================

    public static Table<SpatialAttribute> parseCsv(Path path) throws IOException {
        return parseCsv(path, StandardCharsets.UTF_8);
    }

    public static Table<SpatialAttribute> parseCsv(Path path, Charset charset) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
            return parseCsv(reader);
        }
    }

    public static Table<SpatialAttribute> parseCsv(InputStream in) throws IOException {
        return parseCsv(in, StandardCharsets.UTF_8);
    }

    public static Table<SpatialAttribute> parseCsv(InputStream in, Charset charset) throws IOException {
        return parseCsv(new BufferedReader(new InputStreamReader(in, charset)));
    }

    public static Table<SpatialAttribute> parseCsv(BufferedReader reader) throws IOException {
        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setReader(reader);
        parser.setNumberOfInfoRows(1);
        parser.setConverter(CSV_STR2ATTR);
        parser.setRowSupplier(ArrayList::new);
        List<List<SpatialAttribute>> data = parser.parseToList(reader);
        Header header = parser.getHeader();

        SimpleTable<SpatialAttribute> table = new SimpleTable<>();
        table.set(header.toArray(), data);
        return table;
    }

    //=========================
    //xlsx
    //=========================

    public static Table<SpatialAttribute> parseXlsx(InputStream in) throws IOException, ParsingException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book);
    }

    public static Table<SpatialAttribute> parseXlsx(Path path) throws IOException, ParsingException, InvalidFormatException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in);
        }
    }

    public static Table<SpatialAttribute> parseXlsx(XSSFWorkbook book) throws ParsingException, IOException, InvalidFormatException {
        XSSFSheet sheet = book.getSheetAt(0);

        XlsxSheetParser<SpatialAttribute> parser = new XlsxSheetParser<>();
        parser.setTextConverter(STR2ATTR);
        parser.setNumericConverter(NUM2ATTR);
        parser.setEmptyConverter(EMPTY2ATTR);
        parser.setNumberOfInfoRows(1);

        XlsxTable<SpatialAttribute> table = new XlsxTable<>();
        table.load(parser, sheet);

        return table;
    }

    public static void writeXlsx(Path target, String sheetName, String info, Table<SpatialAttribute> data) throws IOException {
        XlsxSheetWriter<SpatialAttribute> writer = new XlsxSheetWriter<>();
        writer.setNumericConverter(ATTR2NUM);
        writer.setTextConverter(ATTR2STR);
        writer.write(target, sheetName, Collections.singleton(info), new SimpleHeader(data.getHeader()), data.listTable());
    }
}
