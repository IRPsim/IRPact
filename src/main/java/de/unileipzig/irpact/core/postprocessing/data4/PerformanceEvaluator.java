package de.unileipzig.irpact.core.postprocessing.data4;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.performance.FSAPE;
import de.unileipzig.irpact.commons.performance.MAE;
import de.unileipzig.irpact.commons.performance.PerformanceMetric;
import de.unileipzig.irpact.commons.performance.RMSD;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.xlsx.DefaultXlsxSheetWriter3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.postprocessing.data3.PerformanceCalculator;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PerformanceEvaluator extends AbstractGeneralDataHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PerformanceEvaluator.class);

    protected static final String PERFORMANCE_XLSX = "Performance.xlsx";

    public PerformanceEvaluator(DataProcessor4 processor) {
        super(processor, "Performance");
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected String getResourceKey() {
        return "PERFORMANCE";
    }

    protected String getLocalizedString(String key) {
        return getLocalizedString(FileType.XLSX, key);
    }

    @Override
    public void init() throws Throwable {
    }

    @Override
    public void execute() throws Throwable {
        trace("analyse performance");
        analysePerfomance0();
    }

    protected void analysePerfomance0() throws IOException {
        Product product = processor.getUniqueProduct();
        RealAdoptionData realAdoptionData = processor.getUniqueRealAdoptionData();
        List<String> zips = processor.getAllZips(RAConstants.ZIP);

        Path target = processor.getPathToDownloadDir(PERFORMANCE_XLSX);

        //global sheet
        JsonTableData3 globalSheetData = new JsonTableData3();
        globalSheetData.setString(0, 0, getLocalizedString("columnMetric"));
        globalSheetData.setString(0, 1, getLocalizedString("columnValue"));

        double globalRMSD = calcGlobal(RMSD.INSTANCE, product);
        globalSheetData.setString(1, 0, getLocalizedString("RMSD"));
        globalSheetData.setDouble(1, 1, globalRMSD);

        double globalMAE = calcGlobal(MAE.INSTANCE, product);
        globalSheetData.setString(2, 0, getLocalizedString("MAE"));
        globalSheetData.setDouble(2, 1, globalMAE);

        double globalFSAPE = calcGlobal(FSAPE.INSTANCE, product);
        globalSheetData.setString(3, 0, getLocalizedString("FSAPE"));
        globalSheetData.setDouble(3, 1, globalFSAPE);

        //zip
        JsonTableData3 zipSheetData = new JsonTableData3();
        zipSheetData.setString(0, 1, getLocalizedString("RMSD"));
        zipSheetData.setString(0, 2, getLocalizedString("MAE"));
        zipSheetData.setString(0, 3, getLocalizedString("FSAPE"));

        List<String> invalidZips = new ArrayList<>();
        int row = 0;
        for(String zip: zips) {
            if(!realAdoptionData.hasZip(zip)) {
                invalidZips.add(zip);
                continue;
            }

            row++;
            zipSheetData.setString(row, 0, zip);
            zipSheetData.setDouble(row, 1, calcZIP(RMSD.INSTANCE, product, zip));
            zipSheetData.setDouble(row, 2, calcZIP(MAE.INSTANCE, product, zip));
            zipSheetData.setDouble(row, 3, calcZIP(FSAPE.INSTANCE, product, zip));
        }

        row += 2;
        zipSheetData.setString(row, 0, getLocalizedString("invalid"));
        if(invalidZips.size() > 0) {
            zipSheetData.setString(row, 1, StringUtil.toString(invalidZips));
        }

        //write
        DefaultXlsxSheetWriter3<JsonNode> writer = new DefaultXlsxSheetWriter3<>();
        writer.setCellHandler(DefaultXlsxSheetWriter3.forJson());

        info("write {}", target);
        XSSFWorkbook book = new XSSFWorkbook();
        writer.write(book, getLocalizedString("sheetGlobal"), globalSheetData);
        writer.write(book, getLocalizedString("sheetZIP"), zipSheetData);
        writer.write(target, book);
    }

    protected double calcGlobal(PerformanceMetric metric, Product product) {
        return PerformanceCalculator.calculateGlobal(
                metric,
                processor.getUniqueRealAdoptionData(),
                processor.getEnvironment(),
                product,
                RAConstants.ZIP,
                processor.getAllZips(RAConstants.ZIP),
                processor.getAllSimulationYears()
        );
    }

    protected double calcZIP(PerformanceMetric metric, Product product, String zip) {
        return PerformanceCalculator.calculateZIP(
                metric,
                processor.getUniqueRealAdoptionData(),
                processor.getEnvironment(),
                product,
                RAConstants.ZIP,
                zip,
                processor.getAllSimulationYears()
        );
    }
}
