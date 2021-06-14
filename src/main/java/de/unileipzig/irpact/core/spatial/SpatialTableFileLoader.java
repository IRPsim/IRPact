package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.util.io.Header;
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

    public static final CellValueConverter<String, SpatialAttribute> STR2ATTR =
            (header, columnIndex, value) -> new BasicSpatialStringAttribute(header.getLabel(columnIndex), value);
    public static final CellValueConverter<Number, SpatialAttribute> NUM2ATTR =
            (header, columnIndex, value) -> new BasicSpatialDoubleAttribute(header.getLabel(columnIndex), value.doubleValue());

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
    protected Table<SpatialAttribute> data;
    protected boolean preferCsv = false;

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
        } catch (ParsingException | IOException | InvalidFormatException e) {
            throw new MissingDataException(e);
        }
    }

    @Override
    public SpatialTableFileContent getAllAttributes() {
        if(data == null) {
            throw new IllegalStateException("not initalized");
        }
        return new SpatialTableFileContent(inputFileName, data);
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
                data = parseXlsx(in);
            }
            return true;
        }
        if(loader.hasInternal(csvFile)) {
            LOGGER.trace("load xlsx resource '{}'", csvFile);
            try(InputStream in = loader.getInternalAsStream(csvFile)) {
                data = parseXlsx(in);
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

    private static Table<SpatialAttribute> parseCsv(InputStream in) {
        throw new UnsupportedOperationException();
    }

    private static Table<SpatialAttribute> parseCsv(Path path) {
        throw new UnsupportedOperationException();
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
        parser.setNumberOfInfoRows(1);

        XlsxTable<SpatialAttribute> table = new XlsxTable<>();
        table.load(parser, sheet);

        return table;
    }
}
