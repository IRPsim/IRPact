package de.unileipzig.irpact.core.process.ra.npv;

import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.commons.util.xlsx.XlsxUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class PVFileLoader {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileLoader.class);

    protected ResourceLoader loader;
    protected String inputFileName;
    protected NPVXlsxData data;

    public PVFileLoader() {
    }

    public void setLoader(ResourceLoader loader) {
        this.loader = loader;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public NPVXlsxData getData() {
        return data;
    }

    public void initalize() throws MissingDataException {
        try {
            parse();
        } catch (IOException | InvalidFormatException e) {
            throw new MissingDataException(e);
        }
    }

    private void parse() throws IOException, InvalidFormatException {
        if(loader == null) {
            throw new NullPointerException("loader is null");
        }
        if(inputFileName == null) {
            throw new NullPointerException("input file is null");
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
            return;
        }

        throw new FileNotFoundException("file '" + inputFileName + "' not found");
    }

    //=========================
    //xlsx
    //=========================

    private static NPVXlsxData parseXlsx(InputStream in) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook(in);
        return parseXlsx(book);
    }

    private static NPVXlsxData parseXlsx(XSSFWorkbook book) {
        NPVXlsxData data = new NPVXlsxData();
        data.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
        data.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));
        return data;
    }
}
