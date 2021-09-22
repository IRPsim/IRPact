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
    protected static final String EVALUATION_XLSX = "Evaluierung.xlsx";

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
        trace("isLogResultAdoptionsAll: {}", getSettings().isLogResultAdoptionsAll());
        if(getSettings().isLogResultAdoptionsAll()) {
            logAllAdoptionsXlsx();
        }

        trace("isLogPerformance: {}", getSettings().isLogPerformance());
        if(getSettings().isLogPerformance()) {
            analysePerfomance();
        }

        trace("isLogPhaseTransition: {}", getPostAnalysisData().isLogPhaseTransition());
        if(getPostAnalysisData().isLogPhaseTransition()) {
            logPhaseOverview();
        }

        trace("isLogAnnualInterest: {}", getPostAnalysisData().isLogAnnualInterest());
        if(getPostAnalysisData().isLogAnnualInterest()) {
            logInterest();
        }

        trace("isLogEvaluationData: {}", getPostAnalysisData().isLogEvaluationData());
        if(getPostAnalysisData().isLogEvaluationData()) {
            logEvaluation();
        }
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

        row += 2;
        zipSheetData.setString(row, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PERFORMANCE, "invalid"));
        if(invalidZips.size() > 0) {
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

        List<String> zips = getAllZips(RAConstants.ZIP);
        List<Integer> years = getAllSimulationYears();

        Map<String, JsonTableData3> sheets = new LinkedHashMap<>();
        logGlobalPhaseOverview(years, product, sheets);
        logZipPhaseOverview(zips, years, product, sheets);

        //write
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());

        LOGGER.info("write {}", target);
        writer.write(target, sheets);
    }

    protected void logGlobalPhaseOverview(
            List<Integer> years,
            Product product,
            Map<String, JsonTableData3> sheets) {
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
            Map<Integer, Integer> annualOverview = getPostAnalysisData().getTransitionOverviewForYear(product, year);
            overviewData.setInt(row, 0, year);
            overviewData.setInt(row, 1, annualOverview.getOrDefault(PostAnalysisData.INITIAL_ADOPTED, 0));
            overviewData.setInt(row, 2, annualOverview.getOrDefault(PostAnalysisData.AWARENESS, 0));
            overviewData.setInt(row, 3, annualOverview.getOrDefault(PostAnalysisData.FEASIBILITY, 0));
            overviewData.setInt(row, 4, annualOverview.getOrDefault(PostAnalysisData.DECISION_MAKING, 0));
            overviewData.setInt(row, 5, annualOverview.getOrDefault(PostAnalysisData.ADOPTED, 0));
            row++;
        }
        sheets.put(getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "sheet"), overviewData);
    }

    protected void logZipPhaseOverview(
            List<String> zips,
            List<Integer> years,
            Product product,
            Map<String, JsonTableData3> sheets) {

        CountMap3D<String, Integer, Integer> zipYearPhase = new CountMap3D<>();
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            String zip = getZIP(agent, RAConstants.ZIP);
            for(int year: years) {
                int phase = getPostAnalysisData().getPhaseFor(agent, product, year);
                zipYearPhase.update(zip, year, phase);
            }
        }

        for(String zip: zips) {
            JsonTableData3 zipSheet = new JsonTableData3();
            //header
            zipSheet.setString(0, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "year"));
            zipSheet.setString(0, 1, getNameForPhase(PostAnalysisData.INITIAL_ADOPTED));
            zipSheet.setString(0, 2, getNameForPhase(PostAnalysisData.AWARENESS));
            zipSheet.setString(0, 3, getNameForPhase(PostAnalysisData.FEASIBILITY));
            zipSheet.setString(0, 4, getNameForPhase(PostAnalysisData.DECISION_MAKING));
            zipSheet.setString(0, 5, getNameForPhase(PostAnalysisData.ADOPTED));

            //data
            int row = 0;
            for(int year: years) {
                row++;
                zipSheet.setInt(row, 0, year);
                zipSheet.setInt(row, 1, zipYearPhase.getCount(zip, year, PostAnalysisData.INITIAL_ADOPTED));
                zipSheet.setInt(row, 2, zipYearPhase.getCount(zip, year, PostAnalysisData.AWARENESS));
                zipSheet.setInt(row, 3, zipYearPhase.getCount(zip, year, PostAnalysisData.FEASIBILITY));
                zipSheet.setInt(row, 4, zipYearPhase.getCount(zip, year, PostAnalysisData.DECISION_MAKING));
                zipSheet.setInt(row, 5, zipYearPhase.getCount(zip, year, PostAnalysisData.ADOPTED));
            }
            sheets.put(zip, zipSheet);
        }
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
                int count = getPostAnalysisData().getCumulatedAnnualInterestCount(product, year, interest);
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
                double interest = getPostAnalysisData().getAnnualInterest(agent, product, year);
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

    protected void logEvaluation() {
        try {
            trace("log evaluation");
            logEvaluation0();
        } catch (Throwable t) {
            error("error while running 'logEvaluation'", t);
        }
    }

    protected void logEvaluation0() throws IOException {
        List<Product> products = getAllProducts();
        if(products.size() != 1) {
            throw new IllegalArgumentException("products");
        }
        Product product = products.get(0);

        Path target = getPath(EVALUATION_XLSX);

        List<Integer> years = getAllSimulationYears();

        Map<String, JsonTableData3> sheets = new LinkedHashMap<>();
        for(EvaluationType type: EvaluationType.values()) {
            logEvaluationData(type, years, product, sheets);
        }

        LOGGER.info("write {}", target);
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());
        writer.write(target, sheets);
    }

    /**
     * @author Daniel Abitz
     */
    private enum EvaluationType {
        a,
        b,
        c,
        d,
        B
    }

    protected String getSheetName(EvaluationType type) {
        String key;
        switch (type) {
            case a:
                key = "aSheet";
                break;
            case b:
                key = "bSheet";
                break;
            case c:
                key = "cSheet";
                break;
            case d:
                key = "dSheet";
                break;
            case B:
                key = "BSheet";
                break;
            default:
                throw new IllegalArgumentException("unsupported: " + type);
        }
        return getLocalizedString(FileType.XLSX, DataToAnalyse.EVALUATION, key);
    }

    protected int getEvaluationCount(EvaluationType type, PostAnalysisData.EvaluationData data) {
        switch (type) {
            case a:
                return data.countA();
            case b:
                return data.countB();
            case c:
                return data.countC();
            case d:
                return data.countD();
            case B:
                return data.countAdoptionFactor();
            default:
                throw new IllegalArgumentException("unsupported: " + type);
        }
    }

    protected void logEvaluationData(
            EvaluationType type,
            List<Integer> years,
            Product product,
            Map<String, JsonTableData3> sheets) {

        NavigableSet<PostAnalysisData.Bucket> buckets = getPostAnalysisData().getBuckets();

        JsonTableData3 sheetData = new JsonTableData3();

        int column = 0;
        for(int year: years) {
            column++;
            sheetData.setInt(0, column, year);

            int row = 0;
            for(PostAnalysisData.Bucket bucket: buckets) {
                row++;
                sheetData.setString(row, 0, bucket.print(getPostAnalysisData().getEvaluationBucketFormatter()));

                PostAnalysisData.EvaluationData data = getPostAnalysisData().getEvaluationData(product, year, bucket);
                int count = data == null
                        ? 0
                        : getEvaluationCount(type, data);
                sheetData.setInt(row, column, count);
            }
        }

        sheets.put(getSheetName(type), sheetData);
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
