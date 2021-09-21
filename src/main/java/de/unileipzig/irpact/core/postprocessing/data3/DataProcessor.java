package de.unileipzig.irpact.core.postprocessing.data3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.performance.FSAPE;
import de.unileipzig.irpact.commons.performance.MAE;
import de.unileipzig.irpact.commons.performance.PerformanceMetric;
import de.unileipzig.irpact.commons.performance.RMSD;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.count.CountMap3D;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetWriter3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.PostAnalysisData;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class DataProcessor extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DataProcessor.class);

    protected static final String RESULT_BASENAME = "result";
    protected static final String RESULT_EXTENSION = "yaml";

    protected static final String ALL_ADOPTIONS_XLSX = "Alle_Adoptionen.xlsx";
    protected static final String PERFORMANCE_XLSX = "Performance.xlsx";
    protected static final String PHASE_OVERVIEW_XLSX = "Phasenuebersicht.xlsx";
    protected static final String INTEREST_XLSX = "Interesse.xlsx";

    public DataProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        super(metaData, clOptions, inRoot, environment);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void execute() {
        try {
            execute0();
        } catch (Throwable t) {
            error("error while executing DataProcessor", t);
        } finally {
            cleanUp();
        }
    }

    protected void execute0() {
        trace("isLogResultAdoptionsZip: {}", getSettings().isLogResultAdoptionsZip());

        trace("isLogResultAdoptionsZipPhase: {}", getSettings().isLogResultAdoptionsZipPhase());

        trace("isLogResultAdoptionsAll: {}", getSettings().isLogResultAdoptionsAll());
        if(getSettings().isLogResultAdoptionsAll()) {
            logAllAdoptionsXlsx();
        }

        //TODO
        analysePerfomance();
        logPhaseOverview();
        logInterest();
    }

    protected void cleanUp() {
    }

    //=========================
    //out
    //=========================

    protected void logAllAdoptionsXlsx() {
        try {
            logAllAdoptionsXlsx0();
        } catch (Throwable t) {
            error("error while running 'logAllAdoptionsXlsx'", t);
        }
    }

    protected void logAllAdoptionsXlsx0() throws IOException {
//        AllAdoptions2 analyser = new AllAdoptions2();
//        analyser.apply(environment);
//        analyser.setLocalizedData(getLocalizedResultData());
//        analyser.setYears(getAllSimulationYears());
//
//        XlsxVarCollectionWriter writer = new XlsxVarCollectionWriter();
//        writer.setTarget(getTargetDir().resolve(ALL_ADOPTIONS_XLSX));
//        analyser.writeXlsx(writer);
    }

    protected void analysePerfomance() {
        try {
            trace("analyse performance");
            analysePerfomance0();
        } catch (Throwable t) {
            error("error while running 'analysePerfomance'", t);
        }
    }

    protected void analysePerfomance0() throws IOException {
        List<Product> products = getAllProducts();
        if(products.size() != 1) {
            throw new IllegalArgumentException("products");
        }
        Product product = products.get(0);

        Path target = getPath(PERFORMANCE_XLSX);

        List<String> zips = getAllZips(RAConstants.ZIP);
        RealAdoptionData realAdoptionData = getRealAdoptionData();

        //global sheet
        JsonTableData3 globalSheetData = new JsonTableData3();
        globalSheetData.setString(0, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "columnMetric"));
        globalSheetData.setString(0, 1, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "columnValue"));

        double globalRMSD = calcGlobal(RMSD.INSTANCE, product);
        globalSheetData.setString(1, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "RMSD"));
        globalSheetData.setDouble(1, 1, globalRMSD);

        double globalMAE = calcGlobal(MAE.INSTANCE, product);
        globalSheetData.setString(2, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "MAE"));
        globalSheetData.setDouble(2, 1, globalMAE);


        double globalFSAPE = calcGlobal(FSAPE.INSTANCE, product);
        globalSheetData.setString(3, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "FSAPE"));
        globalSheetData.setDouble(3, 1, globalFSAPE);

        //zip
        JsonTableData3 zipSheetData = new JsonTableData3();
        zipSheetData.setString(0, 1, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "RMSD"));
        zipSheetData.setString(0, 2, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "MAE"));
        zipSheetData.setString(0, 3, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "FSAPE"));

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

        if(invalidZips.size() > 0) {
            row += 2;
            zipSheetData.setString(row, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "invalid"));
            zipSheetData.setString(row, 1, StringUtil.toString(invalidZips));
        }

        //write
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());

        LOGGER.info("write {}", target);
        XSSFWorkbook book = new XSSFWorkbook();
        writer.write(book, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "sheetGlobal"), globalSheetData);
        writer.write(book, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "sheetZIP"), zipSheetData);
        writer.write(target, book);
    }

    protected double calcGlobal(PerformanceMetric metric, Product product) {
        return PerformanceCalculator.calculateGlobal(
                metric,
                getRealAdoptionData(),
                environment,
                product,
                RAConstants.ZIP,
                getAllZips(RAConstants.ZIP),
                getAllSimulationYears()
        );
    }

    protected double calcZIP(PerformanceMetric metric, Product product, String zip) {
        return PerformanceCalculator.calculateZIP(
                metric,
                getRealAdoptionData(),
                environment,
                product,
                RAConstants.ZIP,
                zip,
                getAllSimulationYears()
        );
    }

    protected void logPhaseOverview() {
        try {
            trace("log phase overview");
            logPhaseOverview0();
        } catch (Throwable t) {
            error("error while running 'logPhaseOverview'", t);
        }
    }

    protected void logPhaseOverview0() throws IOException {
        List<Product> products = getAllProducts();
        if(products.size() != 1) {
            throw new IllegalStateException("requires exactly one product");
        }
        Product product = products.get(0);

        Path target = getPath(PHASE_OVERVIEW_XLSX);

        List<Integer> years = getAllSimulationYears();
        JsonTableData3 overviewData = new JsonTableData3();
        //header
        overviewData.setString(0, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "year"));
        overviewData.setString(0, 1, getNameForPhase(PostAnalysisData.INITIAL_ADOPTED));
        overviewData.setString(0, 2, getNameForPhase(PostAnalysisData.AWARENESS));
        overviewData.setString(0, 3, getNameForPhase(PostAnalysisData.FEASIBILITY));
        overviewData.setString(0, 4, getNameForPhase(PostAnalysisData.DECISION_MAKING));
        overviewData.setString(0, 5, getNameForPhase(PostAnalysisData.ADOPTED));
        //data
        int row = 1;
        for(int year: years) {
            Map<Integer, Integer> annualOverview = environment.getPostAnalysisData().getTransitionOverviewForYear(product, year);
            overviewData.setInt(row, 0, year);
            overviewData.setInt(row, 1, annualOverview.getOrDefault(PostAnalysisData.INITIAL_ADOPTED, 0));
            overviewData.setInt(row, 2, annualOverview.getOrDefault(PostAnalysisData.AWARENESS, 0));
            overviewData.setInt(row, 3, annualOverview.getOrDefault(PostAnalysisData.FEASIBILITY, 0));
            overviewData.setInt(row, 4, annualOverview.getOrDefault(PostAnalysisData.DECISION_MAKING, 0));
            overviewData.setInt(row, 5, annualOverview.getOrDefault(PostAnalysisData.ADOPTED, 0));
            row++;
        }

        //write
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());

        LOGGER.info("write {}", target);
        XSSFWorkbook book = new XSSFWorkbook();
        writer.write(book, getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "sheet"), overviewData);
        writer.write(target, book);
    }

    protected String getNameForPhase(int phase) {
        String key;
        switch (phase) {
            case PostAnalysisData.INITIAL_ADOPTED:
                key = "phase0";
                break;

            case PostAnalysisData.AWARENESS:
                key = "phase1";
                break;

            case PostAnalysisData.FEASIBILITY:
                key = "phase2";
                break;

            case PostAnalysisData.DECISION_MAKING:
                key = "phase3";
                break;

            case PostAnalysisData.ADOPTED:
                key = "phase4";
                break;

            default:
                throw new IllegalArgumentException("unsupported phase:" + phase);
        }
        return getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, key);
    }

    protected void logInterest() {
        try {
            trace("log interest");
            logInterest0();
        } catch (Throwable t) {
            error("error while running 'logInterest'", t);
        }
    }

    protected void logInterest0() throws IOException {
        List<Product> products = getAllProducts();
        if(products.size() != 1) {
            throw new IllegalArgumentException("products");
        }
        Product product = products.get(0);

        Path target = getPath(INTEREST_XLSX);

        List<Integer> years = getAllSimulationYears();
        List<Double> interestValues = getInterestValues(product);
        List<String> zips = getAllZips(RAConstants.ZIP);

        Map<String, JsonTableData3> sheets = new LinkedHashMap<>();
        logGlobalInterest(years, interestValues, product, sheets);
        logZipInterest(zips, years, interestValues, product, sheets);

        LOGGER.info("write {}", target);
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());
        writer.write(target, sheets);
    }

    protected void logGlobalInterest(
            List<Integer> years,
            List<Double> interestValues,
            Product product,
            Map<String, JsonTableData3> sheets) {
        JsonTableData3 globalSheet = new JsonTableData3();
        //header
        int column = 0;
        for(int year: years) {
            column++;
            globalSheet.setInt(0, column, year);
        }
        int row = 0;
        for(double interest: interestValues) {
            row++;
            globalSheet.setDouble(row, 0, interest);
        }
        //data
        column = 0;
        for(int year: years) {
            column++;
            row = 0;
            for(double interest: interestValues) {
                row++;
                int count = environment.getPostAnalysisData().getCumulatedAnnualInterestCount(product, year, interest);
                globalSheet.setInt(row, column, count);
            }
        }
        sheets.put(getLocalizedString(FileType.XLSX, DataToAnalyse.INTEREST, "sheetGlobal"), globalSheet);
    }

    protected void logZipInterest(
            List<String> zips,
            List<Integer> years,
            List<Double> interestValues,
            Product product,
            Map<String, JsonTableData3> sheets) {

        CountMap3D<String, Integer, Double> zipYearInterest = new CountMap3D<>();
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            String zip = getZIP(agent, RAConstants.ZIP);
            for(int year: years) {
                double interest = environment.getPostAnalysisData().getAnnualInterest(agent, product, year);
                zipYearInterest.update(zip, year, interest);
            }
        }

        for(String zip: zips) {
            JsonTableData3 zipSheet = new JsonTableData3();
            //header
            int column = 0;
            for(int year: years) {
                column++;
                zipSheet.setInt(0, column, year);
            }
            int row = 0;
            for(double interest: interestValues) {
                row++;
                zipSheet.setDouble(row, 0, interest);
            }

            //data
            column = 0;
            for(int year: years) {
                column++;
                row = 0;
                for(double interest: interestValues) {
                    row++;
                    int count = zipYearInterest.getCount(zip, year, interest);
                    zipSheet.setInt(row, column, count);
                }
            }
            sheets.put(zip, zipSheet);
        }
    }

    //=========================
    //util
    //=========================

    protected RealAdoptionData getRealAdoptionData() {
        List<InRealAdoptionDataFile> realAdoptionDataFiles = inRoot.findFiles(InRealAdoptionDataFile.class);
        if(realAdoptionDataFiles.isEmpty()) throw new IllegalArgumentException("missing real adoption data");
        if(realAdoptionDataFiles.size() > 1) throw new IllegalArgumentException("too many adoption files");

        InRealAdoptionDataFile file = realAdoptionDataFiles.get(0);
        return getRealAdoptionData(file);
    }

    protected Path getPath(String name) throws IOException {
        return clOptions.getCreatedDownloadDir().resolve(name);
    }

    protected void loadLocalizedData() throws IOException {
        ObjectNode root = tryLoadYaml(RESULT_BASENAME, RESULT_EXTENSION);
        if(root == null) {
            throw new IOException("missing resource: " + LocaleUtil.buildName(RESULT_BASENAME, metaData.getLocale(), RESULT_EXTENSION));
        }
        localizedData = new JsonResource(root);
    }

    protected JsonResource localizedData;
    public JsonResource getLocalizedData() throws UncheckedIOException {
        if(localizedData == null) {
            try {
                loadLocalizedData();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return localizedData;
    }

    protected Object buildKey(FileType file, DataToAnalyse mode, String key) {
        return new String[] {file.name(), mode.name(), key};
    }

    protected String getLocalizedString(FileType file, DataToAnalyse mode, String key) {
        return getLocalizedData().getString(buildKey(file, mode, key));
    }

    protected String getLocalizedFormattedString(FileType file, DataToAnalyse mode, String key, Object... args) {
        return getLocalizedData().getFormattedString(buildKey(file, mode, key), args);
    }
}
