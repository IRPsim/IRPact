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
import de.unileipzig.irpact.commons.util.io3.csv.CsvParser;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetWriter3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.DataAnalyser;
import de.unileipzig.irpact.core.logging.DataLogger;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class DataProcessor extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DataProcessor.class);

    protected static final String RESULT_RES_BASENAME = "result";
    protected static final String RESULT_RES_EXTENSION = "yaml";

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

        trace("isLogPhaseTransition: {}", getDataAnalyser().isLogPhaseTransition());
        if(getDataAnalyser().isLogPhaseTransition()) {
            logPhaseOverview();
        }

        trace("isLogAnnualInterest: {}", getDataAnalyser().isLogAnnualInterest());
        if(getDataAnalyser().isLogAnnualInterest()) {
            logInterest();
        }

        trace("isLogEvaluationData: {}", getDataAnalyser().isLogEvaluationData());
        if(getDataAnalyser().isLogEvaluationData()) {
            logEvaluationData();
        }

        trace("isLogEvaluation: {}", getDataLogger().isLogEvaluation());
        if(getDataLogger().isLogEvaluation()) {
            logEvaluation();
        }

        trace("isLogFinancialComponent: {}", getDataLogger().isLogFinancialComponent());
        if(getDataLogger().isLogFinancialComponent()) {
            logFinancialComponent();
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

        info("write {}", target);
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

        info("write {}", target);
        writer.write(target, sheets);
    }

    protected void logGlobalPhaseOverview(
            List<Integer> years,
            Product product,
            Map<String, JsonTableData3> sheets) {
        JsonTableData3 overviewData = new JsonTableData3();
        //header
        overviewData.setString(0, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "year"));
        overviewData.setString(0, 1, getNameForPhase(DataAnalyser.Phase.INITIAL_ADOPTED));
        overviewData.setString(0, 2, getNameForPhase(DataAnalyser.Phase.AWARENESS));
        overviewData.setString(0, 3, getNameForPhase(DataAnalyser.Phase.FEASIBILITY));
        overviewData.setString(0, 4, getNameForPhase(DataAnalyser.Phase.DECISION_MAKING));
        overviewData.setString(0, 5, getNameForPhase(DataAnalyser.Phase.ADOPTED));
        //data
        int row = 1;
        for(int year: years) {
            Map<DataAnalyser.Phase, Integer> annualOverview = getDataAnalyser().getTransitionOverviewForYear(product, year);
            overviewData.setInt(row, 0, year);
            overviewData.setInt(row, 1, annualOverview.getOrDefault(DataAnalyser.Phase.INITIAL_ADOPTED, 0));
            overviewData.setInt(row, 2, annualOverview.getOrDefault(DataAnalyser.Phase.AWARENESS, 0));
            overviewData.setInt(row, 3, annualOverview.getOrDefault(DataAnalyser.Phase.FEASIBILITY, 0));
            overviewData.setInt(row, 4, annualOverview.getOrDefault(DataAnalyser.Phase.DECISION_MAKING, 0));
            overviewData.setInt(row, 5, annualOverview.getOrDefault(DataAnalyser.Phase.ADOPTED, 0));
            row++;
        }
        sheets.put(getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "sheet"), overviewData);
    }

    protected void logZipPhaseOverview(
            List<String> zips,
            List<Integer> years,
            Product product,
            Map<String, JsonTableData3> sheets) {

        CountMap3D<String, Integer, DataAnalyser.Phase> zipYearPhase = new CountMap3D<>();
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            String zip = getZIP(agent, RAConstants.ZIP);
            for(int year: years) {
                DataAnalyser.Phase phase = getDataAnalyser().getPhaseFor(agent, product, year);
                zipYearPhase.update(zip, year, phase);
            }
        }

        for(String zip: zips) {
            JsonTableData3 zipSheet = new JsonTableData3();
            //header
            zipSheet.setString(0, 0, getLocalizedString(FileType.XLSX, DataToAnalyse.PHASE_OVERVIEW, "year"));
            zipSheet.setString(0, 1, getNameForPhase(DataAnalyser.Phase.INITIAL_ADOPTED));
            zipSheet.setString(0, 2, getNameForPhase(DataAnalyser.Phase.AWARENESS));
            zipSheet.setString(0, 3, getNameForPhase(DataAnalyser.Phase.FEASIBILITY));
            zipSheet.setString(0, 4, getNameForPhase(DataAnalyser.Phase.DECISION_MAKING));
            zipSheet.setString(0, 5, getNameForPhase(DataAnalyser.Phase.ADOPTED));

            //data
            int row = 0;
            for(int year: years) {
                row++;
                zipSheet.setInt(row, 0, year);
                zipSheet.setInt(row, 1, zipYearPhase.getCount(zip, year, DataAnalyser.Phase.INITIAL_ADOPTED));
                zipSheet.setInt(row, 2, zipYearPhase.getCount(zip, year, DataAnalyser.Phase.AWARENESS));
                zipSheet.setInt(row, 3, zipYearPhase.getCount(zip, year, DataAnalyser.Phase.FEASIBILITY));
                zipSheet.setInt(row, 4, zipYearPhase.getCount(zip, year, DataAnalyser.Phase.DECISION_MAKING));
                zipSheet.setInt(row, 5, zipYearPhase.getCount(zip, year, DataAnalyser.Phase.ADOPTED));
            }
            sheets.put(zip, zipSheet);
        }
    }

    protected String getNameForPhase(DataAnalyser.Phase phase) {
        String key;
        switch (phase) {
            case INITIAL_ADOPTED:
                key = "phase0";
                break;

            case AWARENESS:
                key = "phase1";
                break;

            case FEASIBILITY:
                key = "phase2";
                break;

            case DECISION_MAKING:
                key = "phase3";
                break;

            case ADOPTED:
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

        info("write {}", target);
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
                int count = getDataAnalyser().getCumulatedAnnualInterestCount(product, year, interest);
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
                double interest = getDataAnalyser().getAnnualInterest(agent, product, year);
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

    protected void logEvaluationData() {
        try {
            trace("log evaluation data");
            logEvaluationData0();
        } catch (Throwable t) {
            error("error while running 'logEvaluationData'", t);
        }
    }

    protected void logEvaluationData0() throws IOException {
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

        info("write {}", target);
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
        aa,
        bb,
        cc,
        dd,
        weightedAA,
        weightedBB,
        weightedCC,
        weightedDD,
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
            case aa:
                key = "aaSheet";
                break;
            case bb:
                key = "bbSheet";
                break;
            case cc:
                key = "ccSheet";
                break;
            case dd:
                key = "ddSheet";
                break;
            case weightedAA:
                key = "weightedAASheet";
                break;
            case weightedBB:
                key = "weightedBBSheet";
                break;
            case weightedCC:
                key = "weightedCCSheet";
                break;
            case weightedDD:
                key = "weightedDDSheet";
                break;
            case B:
                key = "BSheet";
                break;
            default:
                throw new IllegalArgumentException("unsupported: " + type);
        }
        return getLocalizedString(FileType.XLSX, DataToAnalyse.EVALUATION, key);
    }

    protected int getEvaluationCount(EvaluationType type, DataAnalyser.EvaluationData data) {
        switch (type) {
            case a:
                return data.countA();
            case b:
                return data.countB();
            case c:
                return data.countC();
            case d:
                return data.countD();
            case aa:
                return data.countAA();
            case bb:
                return data.countBB();
            case cc:
                return data.countCC();
            case dd:
                return data.countDD();
            case weightedAA:
                return data.countWeightedAA();
            case weightedBB:
                return data.countWeightedBB();
            case weightedCC:
                return data.countWeightedCC();
            case weightedDD:
                return data.countWeightedDD();
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

        NavigableSet<DataAnalyser.Bucket> buckets = getDataAnalyser().getBuckets();

        JsonTableData3 sheetData = new JsonTableData3();

        int column = 0;
        for(int year: years) {
            column++;
            sheetData.setInt(0, column, year);

            int row = 0;
            for(DataAnalyser.Bucket bucket: buckets) {
                row++;
                sheetData.setString(row, 0, bucket.print(getDataAnalyser().getEvaluationBucketFormatter()));

                DataAnalyser.EvaluationData data = getDataAnalyser().getEvaluationData(product, year, bucket);
                int count = data == null
                        ? 0
                        : getEvaluationCount(type, data);
                sheetData.setInt(row, column, count);
            }
        }

        sheets.put(getSheetName(type), sheetData);
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
        Path csvFile = clOptions.getCreatedDownloadDir().resolve(IRPact.ALL_EVAL_CSV);
        Path xlsxFile = clOptions.getCreatedDownloadDir().resolve(IRPact.ALL_EVAL_XLSX);

        //read
        info("read {}", csvFile);
        CsvParser<JsonNode> csvParser = new CsvParser<>();
        csvParser.setValueGetter(CsvParser.forJson());
        JsonTableData3 data = new JsonTableData3(csvParser.parseToList(csvFile, StandardCharsets.UTF_8));


        trace("map csv to xlsx");
        data.mapColumn(3, 0, node -> {
            String str = node.textValue();
            if(DataLogger.NO_VALUE.equals(str)) {
                return node;
            } else {
                return data.getCreator().numberNode(Integer.parseInt(str));
            }
        });

        for(int c = 4; c < 28; c++) {
            data.mapColumn(c, 0, node -> {
                String str = node.textValue();
                if(DataLogger.NO_VALUE.equals(str)) {
                    return node;
                } else {
                    return data.getCreator().numberNode(Double.parseDouble(str));
                }
            });
        }

        //header
        data.insertRow(0);
        data.setString(0, 0, getAllEvalString("columnAgent"));
        data.setString(0, 1, getAllEvalString("columnProduct"));
        data.setString(0, 2, getAllEvalString("columnTime"));
        data.setString(0, 3, getAllEvalString("columnYear"));
        data.setString(0, 4, getAllEvalString("columnAWeight"));
        data.setString(0, 5, getAllEvalString("columnBWeight"));
        data.setString(0, 6, getAllEvalString("columnCWeight"));
        data.setString(0, 7, getAllEvalString("columnDWeight"));
        data.setString(0, 8, getAllEvalString("columnA"));
        data.setString(0, 9, getAllEvalString("columnB"));
        data.setString(0, 10, getAllEvalString("columnC"));
        data.setString(0, 11, getAllEvalString("columnD"));
        data.setString(0, 12, getAllEvalString("columnAValue"));
        data.setString(0, 13, getAllEvalString("columnBValue"));
        data.setString(0, 14, getAllEvalString("columnCValue"));
        data.setString(0, 15, getAllEvalString("columnDValue"));
        data.setString(0, 16, getAllEvalString("columnAA"));
        data.setString(0, 17, getAllEvalString("columnBB"));
        data.setString(0, 18, getAllEvalString("columnCC"));
        data.setString(0, 19, getAllEvalString("columnDD"));
        data.setString(0, 20, getAllEvalString("columnWeightedAA"));
        data.setString(0, 21, getAllEvalString("columnWeightedBB"));
        data.setString(0, 22, getAllEvalString("columnWeightedCC"));
        data.setString(0, 23, getAllEvalString("columnWeightedDD"));
        data.setString(0, 24, getAllEvalString("columnFinancialThreshold"));
        data.setString(0, 25, getAllEvalString("columnFinancialValue"));
        data.setString(0, 26, getAllEvalString("columnAdoptionThreshold"));
        data.setString(0, 27, getAllEvalString("columnAdoptionValue"));

        //write
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());

        info("write {}", xlsxFile);
        writer.write(xlsxFile, getAllEvalString("sheet"), data);

        //cleanup
        getDataLogger().finishLogEvaluation();
        try {
            trace("delete: {}", getDataLogger().getLogEvaluationTarget());
            Files.deleteIfExists(getDataLogger().getLogEvaluationTarget());
        } catch (IOException e) {
            warn("deleting failed", e);
        }
    }

    protected String getAllEvalString(String key) {
        return getLocalizedString(FileType.XLSX, DataToAnalyse.ALL_EVALUATION, key);
    }

    protected void logFinancialComponent() {
        try {
            trace("log financial component");
            logFinancialComponent0();
        } catch (Throwable t) {
            error("error while running 'logFinancialComponent'", t);
        }
    }

    protected void logFinancialComponent0() throws IOException {
        Path csvFile = clOptions.getCreatedDownloadDir().resolve(IRPact.FIN_CSV);
        Path xlsxFile = clOptions.getCreatedDownloadDir().resolve(IRPact.FIN_XLSX);

        //read
        info("read {}", csvFile);
        CsvParser<JsonNode> csvParser = new CsvParser<>();
        csvParser.setValueGetter(CsvParser.forJson());
        JsonTableData3 data = new JsonTableData3(csvParser.parseToList(csvFile, StandardCharsets.UTF_8));


        trace("map csv to xlsx");
        data.mapColumn(3, 0, node -> {
            String str = node.textValue();
            if(DataLogger.NO_VALUE.equals(str)) {
                return node;
            } else {
                return data.getCreator().numberNode(Integer.parseInt(str));
            }
        });

        for(int c = 4; c < 16; c++) {
            data.mapColumn(c, 0, node -> {
                String str = node.textValue();
                if(DataLogger.NO_VALUE.equals(str)) {
                    return node;
                } else {
                    return data.getCreator().numberNode(Double.parseDouble(str));
                }
            });
        }

        //header
        data.insertRow(0);
        data.setString(0, 0, getFinString("columnAgent"));
        data.setString(0, 1, getFinString("columnProduct"));
        data.setString(0, 2, getFinString("columnTime"));
        data.setString(0, 3, getFinString("columnYear"));
        data.setString(0, 4, getFinString("columnLogisticFactor"));
        data.setString(0, 5, getFinString("columnWeightFt"));
        data.setString(0, 6, getFinString("columnFtAvg"));
        data.setString(0, 7, getFinString("columnFtThis"));
        data.setString(0, 8, getFinString("columnFt"));
        data.setString(0, 9, getFinString("columnLogisticFt"));
        data.setString(0, 10, getFinString("columnWeightNpv"));
        data.setString(0, 11, getFinString("columnMpvAvg"));
        data.setString(0, 12, getFinString("columnNpvThis"));
        data.setString(0, 13, getFinString("columnNpv"));
        data.setString(0, 14, getFinString("columnLogisticNpv"));
        data.setString(0, 15, getFinString("columnFin"));

        //write
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());

        info("write {}", xlsxFile);
        writer.write(xlsxFile, getFinString("sheet"), data);

        //cleanup
        getDataLogger().finishLogFinancialComponent();
        try {
            trace("delete: {}", getDataLogger().getLogFinancialComponentTarget());
            Files.deleteIfExists(getDataLogger().getLogFinancialComponentTarget());
        } catch (IOException e) {
            warn("deleting failed", e);
        }
    }

    protected String getFinString(String key) {
        return getLocalizedString(FileType.XLSX, DataToAnalyse.FINANCIAL_COMPONENT, key);
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
        ObjectNode root = tryLoadYaml(RESULT_RES_BASENAME, RESULT_RES_EXTENSION);
        if(root == null) {
            throw new IOException("missing resource: " + LocaleUtil.buildName(RESULT_RES_BASENAME, metaData.getLocale(), RESULT_RES_EXTENSION));
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
