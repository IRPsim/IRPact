package de.unileipzig.irpact.core.postprocessing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.csv.CsvParser;
import de.unileipzig.irpact.commons.util.io3.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetWriter3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.logging.data.DataLogger;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.FallbackAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data4.RAHelperAPIInstance;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public abstract class PostProcessor implements LoggingHelper {

    protected static final String RPLOTS_PDF = "Rplots.pdf";

    //protected static final Map<InRealAdoptionDataFile, RealAdoptionData> globalAdoptionDataCache = new HashMap<>();
    protected static final FallbackAdoptionData PLACEHOLDER_REAL_DATA = new FallbackAdoptionData(0);

    protected final Map<Object, Object> DATA_CACHE = new HashMap<>();

    protected final RAHelperAPIInstance RA_HELPER = new RAHelperAPIInstance("RA_HELPER");
    protected MetaData metaData;
    protected MainCommandLineOptions clOptions;
    protected InRoot inRoot;
    protected SimulationEnvironment environment;
    protected JsonResource localizedData;

    public PostProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        this.metaData = metaData;
        this.clOptions = clOptions;
        this.inRoot = inRoot;
        this.environment = environment;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    public RAHelperAPI2 getRAHelperAPI() {
        return RA_HELPER;
    }

    public abstract void execute();

    protected void cleanUp() {
        deleteRplotsPdf();
    }

    protected void deleteRplotsPdf() {
        delete(Paths.get(RPLOTS_PDF));
        delete(clOptions.getOutputDir().resolve(RPLOTS_PDF));
        delete(clOptions.getDownloadDir().resolve(RPLOTS_PDF));
    }

    protected void delete(Path path) {
        try {
            if(Files.exists(path)) {
                Files.deleteIfExists(path);
                trace("deleted: '{}'", path);
            }
        } catch (IOException e) {
            error("deleting '" + path + "' failed", e);
        }
    }

    protected Settings getSettings() {
        return environment.getSettings();
    }

    public DataAnalyser getDataAnalyser() {
        return environment.getDataAnalyser();
    }

    public DataLogger getDataLogger() {
        return environment.getDataLogger();
    }

    public Path getTargetDir() throws IOException {
        return clOptions.getCreatedDownloadDir();
    }

    protected List<Integer> years;
    public List<Integer> getAllSimulationYears() {
        if(years == null) {
            int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
            int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
            years = IntStream.rangeClosed(firstYear, lastYear)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return years;
    }

    protected List<Integer> yearsWithPrior;
    public List<Integer> getAllSimulationYearsPrior() {
        if(yearsWithPrior == null) {
            List<Integer> years = new ArrayList<>(getAllSimulationYears());
            years.add(0, years.get(0) - 1);
            yearsWithPrior = years;
        }
        return yearsWithPrior;
    }

    public int getFirstSimulationYear() {
        return metaData.getOldestRunInfo().getFirstSimulationYear();
    }

    protected double getLargestInterestThreshold(Product product) {
        double threshold = Double.NaN;
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                ProductInterest interest = ca.getProductInterest();
                if(interest.hasThreshold(product.getGroup())) {
                    if(Double.isNaN(threshold) || threshold < interest.getThreshold(product.getGroup())) {
                        threshold = interest.getThreshold(product.getGroup());
                    }
                }
            }
        }
        return threshold;
    }

    protected Map<Product, List<Double>> allInterestValues = new HashMap<>();
    public List<Double> getInterestValues(Product product) {
        List<Double> values = allInterestValues.get(product);
        if(values == null) {
            values = new ArrayList<>();
            double largestThreshold = getLargestInterestThreshold(product);
            if(Double.isNaN(largestThreshold)) {
                throw new IllegalArgumentException("missing threshold for produkt '" + product.getName() + "'");
            }
            for(double t = 0.0; t <= largestThreshold; t++) {
                values.add(t);
            }
            allInterestValues.put(product, values);
        }
        return values;
    }

    protected List<String> zips;
    public List<String> getAllZips(String key) {
        if(zips == null) {
            zips = environment.getAgents().streamConsumerAgents()
                    .filter(agent -> agent.hasAnyAttribute(key))
                    .map(agent -> {
                        Attribute attr = agent.findAttribute(key);
                        return attr.asValueAttribute().getValueAsString();
                    })
                    .distinct()
                    .collect(Collectors.toList());
        }
        return zips;
    }

    public Set<String> getValidZips(RealAdoptionData realData, String key) {
        List<String> allZips = getAllZips(key);
        Set<String> validZips = new HashSet<>();
        realData.getValidZips(allZips, validZips);
        return validZips;
    }

    public Set<String> getInvalidZips(RealAdoptionData realData, String key) {
        List<String> allZips = getAllZips(key);
        Set<String> invalidZips = new HashSet<>();
        realData.getInvalidZips(allZips, invalidZips);
        return invalidZips;
    }

    protected List<Product> products;
    public List<Product> getAllProducts() {
        if(products == null) {
            Set<Product> set = new HashSet<>();
            for(ProductGroup pg: environment.getProducts().getGroups()) {
                set.addAll(pg.getProducts());
            }
            products = new ArrayList<>(set);
        }
        return products;
    }

    public Product getSingletonProduct() {
        List<Product> products = getAllProducts();
        if(products.size() == 1) {
            return products.get(0);
        } else {
            throw new IllegalStateException("product count mismatch: " + products);
        }
    }

    protected ObjectNode tryLoadYaml(String baseName, String extension) throws IOException {
        ObjectNode root = tryLoadExternalYaml(baseName, extension);
        if(root != null) return root;
        return tryLoadInternalYaml(baseName, extension);
    }

    protected ObjectNode tryLoadExternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedExternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedExternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedExternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadInternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedInternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedInternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedInternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadYamlAndCloseStream(InputStream in) throws IOException {
        if(in == null) {
            return null;
        }
        try {
            return JsonUtil.read(in, JsonUtil.YAML);
        } finally {
            in.close();
        }
    }

    protected RealAdoptionData getFallbackAdoptionData() {
        return PLACEHOLDER_REAL_DATA;
    }

    protected DataStore getGlobalData() {
        return environment.getGlobalData();
    }

    public RealAdoptionData getRealAdoptionData(InRealAdoptionDataFile file) {
        DataStore globalData = getGlobalData();
        if(globalData.contains(file)) {
            return globalData.getAuto(file);
        } else {
            try {
                getDefaultLogger().warn("try loading '{}'", file.getFileNameWithoutExtension());
                RealAdoptionData adoptionData = file.parse(environment.getResourceLoader());
                globalData.put(file, adoptionData);
                return adoptionData;
            } catch (Throwable t) {
                getDefaultLogger().warn("loading '{}' failed, use fallback data, cause: {}", file.getFileNameWithoutExtension(), StringUtil.printStackTrace(t));
                RealAdoptionData adoptionData = getFallbackAdoptionData();
                globalData.put(file, adoptionData);
                return adoptionData;
            }
        }
    }

    public RealAdoptionData getScaledRealAdoptionData(InRealAdoptionDataFile file) {
        RealAdoptionData realAdoptionData = getRealAdoptionData(file);
        double scaleFactor = getScaleFactor();
        if(scaleFactor == 1.0) {
            return realAdoptionData;
        } else {
            if(!realAdoptionData.hasScaledAdoptionData(scaleFactor)) {
                realAdoptionData.createScaledAdoptionData(scaleFactor, true);
            }
            ScaledRealAdoptionData scaledAdoptionData = realAdoptionData.getScaledAdoptionData(scaleFactor);
            if(scaledAdoptionData.getScale() != scaleFactor) {
                throw new IllegalStateException("scale mismatch: " + scaledAdoptionData.getScale() + " != " + scaleFactor);
            }
            return scaledAdoptionData;
        }
    }

    protected String getZIP(ConsumerAgent agent, String key) {
        return agent.findAttribute(key).asValueAttribute().getStringValue();
    }

    protected double getScaleFactor() {
        return environment.getAgents().getInitialAgentPopulation().hasScale()
                ? environment.getAgents().getInitialAgentPopulation().getScale()
                : 1.0;
    }

    public boolean isCached(Object key) {
        return DATA_CACHE.containsKey(key);
    }

    public void storeInCache(Object key, Object value) {
        DATA_CACHE.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <R> R retrieveFromCache(Object key) {
        return (R) DATA_CACHE.get(key);
    }

    public void removeFromCache(String key) {
        DATA_CACHE.remove(key);
    }

    public void clearCache() {
        DATA_CACHE.clear();
    }

    protected void loadLocalizedData(String baseName, String extension) throws IOException {
        ObjectNode root = tryLoadYaml(baseName, extension);
        if(root == null) {
            throw new IOException("missing resource: " + LocaleUtil.buildName(baseName, metaData.getLocale(), extension));
        }
        localizedData = new JsonResource(root);
    }

    public JsonResource getLocalizedData(String baseName, String extension) throws UncheckedIOException {
        if(localizedData == null) {
            try {
                loadLocalizedData(baseName, extension);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return localizedData;
    }

    public JsonTableData3 loadOrGetCsv(Path path, Charset charset, String delimiter, Object key) throws IOException {
        if(isCached(key)) {
            return retrieveFromCache(key);
        } else {
            JsonTableData3 data = loadCsv(path, charset, delimiter);
            storeInCache(key, data);
            return data;
        }
    }

    public JsonTableData3 loadCsv(Path path, Charset charset, String delimiter) throws IOException {
        CsvParser<JsonNode> parser = new CsvParser<>();
        parser.setValueGetter(CsvParser.forJson());
        parser.setDelimiter(delimiter);
        return new JsonTableData3(parser.parseToList(path, charset));
    }

    public void storeCsv(Path path, Charset charset, String delimiter, JsonTableData3 data) throws IOException {
        CsvPrinter<JsonNode> printer = new CsvPrinter<>();
        printer.setValueSetter(CsvPrinter.forJson);
        printer.setDelimiter(delimiter);
        printer.write(path, charset, data);
    }

    public void storeXlsx(Path path, Map<String, JsonTableData3> sheetData) throws IOException {
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());
        writer.write(path, sheetData);
    }

    public void storeXlsx(Path path, String sheetName, JsonTableData3 data) throws IOException {
        Map<String, JsonTableData3> sheetData = new HashMap<>();
        sheetData.put(sheetName, data);
        storeXlsx(path, sheetData);
    }

    public void storeXlsx(Path path, DateTimeFormatter formatter, Map<String, JsonTableData3> sheetData) throws IOException {
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        XSSFWorkbook book = writer.newBook();
        CellStyle dateStyle = XlsxSheetWriter3.createDefaultDateStyle(book);

        writer.setCellHandler(
                XlsxSheetWriter3.forJson(
                        XlsxSheetWriter3.testTime(formatter),
                        XlsxSheetWriter3.toTime(formatter),
                        XlsxSheetWriter3.toCellStyle(dateStyle)
                )
        );

        writer.write(
                path,
                book,
                sheetData
        );
    }

    public void storeXlsx(Path path, DateTimeFormatter formatter, String sheetName, JsonTableData3 data) throws IOException {
        Map<String, JsonTableData3> sheetData = new HashMap<>();
        sheetData.put(sheetName, data);
        storeXlsx(path, formatter, sheetData);
    }
}
