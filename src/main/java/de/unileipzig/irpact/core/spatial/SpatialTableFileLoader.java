package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.commons.util.xlsx.SimpleXlsxTableParser;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialStringAttribute;
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
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileLoader implements SpatialInformationLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader.class);

    protected ResourceLoader loader;
    protected String inputFileName;
    protected List<List<SpatialAttribute>> data;

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

        String csvFile = inputFileName + ".csv";
        if(loader.hasPath(csvFile)) {
            Path csvPath = loader.get(csvFile);
            LOGGER.trace("load csv file '{}'", csvPath);
            data = parseCsv(csvPath);
            return;
        }
        if(loader.hasResource(csvFile)) {
            LOGGER.trace("load csv resource '{}'", csvFile);
            try(InputStream in = loader.getResourceAsStream(csvFile)) {
                data = parseCsv(in);
            }
            return;
        }

        String xlsxFile = inputFileName + ".xlsx";
        if(loader.hasPath(xlsxFile)) {
            Path xlsxPath = loader.get(xlsxFile);
            LOGGER.trace("load xlsx file '{}'", xlsxPath);
            try(InputStream in = Files.newInputStream(xlsxPath)) {
                data = parseXlsx(in);
            }
            return;
        }
        if(loader.hasResource(xlsxFile)) {
            LOGGER.trace("load xlsx resource '{}'", xlsxFile);
            try(InputStream in = loader.getResourceAsStream(xlsxFile)) {
                data = parseXlsx(in);
            }
            return;
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    //=========================
    //csv
    //=========================

    private static List<List<SpatialAttribute>> parseCsv(InputStream in) {
        throw new UnsupportedOperationException();
    }

    private static List<List<SpatialAttribute>> parseCsv(Path path) {
        throw new UnsupportedOperationException();
    }

    //=========================
    //xlsx
    //=========================

    public static List<List<SpatialAttribute>> parseXlsx(InputStream in) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        XSSFFont font = book.createFont();
        font.setCharSet(FontCharset.ANSI);
        return parseXlsx(book);
    }

    public static List<List<SpatialAttribute>> parseXlsx(Path path) throws IOException {
        try(InputStream in = Files.newInputStream(path)) {
            return parseXlsx(in);
        }
    }

    public static List<List<SpatialAttribute>> parseXlsx(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheetAt(0);

        SimpleXlsxTableParser<SpatialAttribute> parser = new SimpleXlsxTableParser<>();
        parser.setTextConverter((columnIndex, header, value) ->
                new SpatialStringAttribute(header[columnIndex], value));
        parser.setNumbericConverter((columnIndex, header, value) ->
                new SpatialDoubleAttribute(header[columnIndex], value.doubleValue()));
        parser.parse(sheet);

        List<List<SpatialAttribute>> out = parser.getRows();
        parser.reset();
        return out;
    }
}
