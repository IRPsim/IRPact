package de.unileipzig.irpact.core.util.result;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.*;
import de.unileipzig.irpact.core.postprocessing.image.*;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.RFileScript;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class ResultManager implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ResultManager.class);

    protected static final de.unileipzig.irpact.util.R.builder.StringSettings R_STRING_SETTINGS
            = new de.unileipzig.irpact.util.R.builder.StringSettings();
    protected static final de.unileipzig.irpact.util.gnuplot.builder.StringSettings GNUPLOT_STRING_SETTINGS
            = new de.unileipzig.irpact.util.gnuplot.builder.StringSettings();

    protected static final BasicRealAdoptionData PLACEHOLDER_REAL_DATA = new BasicRealAdoptionData(0);

    protected static final String IMAGES_BASENAME = "images";
    protected static final String IMAGES_EXTENSION = "yaml";
    protected static final String RPLOTS_PDF = "Rplots.pdf";

    protected static String defaultSep = ";";
    protected static double defaultLinewidth = 1.0;
    protected LocalizedImageData localizedImageData;

    protected MetaData metaData;
    protected MainCommandLineOptions clOptions;
    protected InRoot inRoot;
    protected SimulationEnvironment environment;

    public ResultManager(
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
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    public void execute() {
        handleResultLogging();
        handleScriptLogging();
        handleImageCreation();
        cleanUp();
    }

    protected void cleanUp() {
        deleteRplotsPdf();
    }

    protected void deleteRplotsPdf() {
        delete(Paths.get(RPLOTS_PDF));
        delete(clOptions.getOutputDir().resolve(RPLOTS_PDF));
        delete(clOptions.getDownloadDir().resolve(RPLOTS_PDF));
    }

    private static void delete(Path path) {
        try {
            if(Files.exists(path)) {
                Files.deleteIfExists(path);
                LOGGER.trace(IRPSection.RESULT, "deleted: '{}'", path);
            }
        } catch (IOException e) {
            LOGGER.error("deleting '" + path + "' failed", e);
        }
    }

    //=========================
    //result
    //=========================

    protected void handleResultLogging() {
        try {
            handleResultLogging0();
        } catch (Throwable t) {
            error("error when running 'handleResultLogging'", t);
        }
    }

    protected void handleResultLogging0() {
        trace("isLogResultAdoptionsZip: {}", getSettings().isLogResultAdoptionsZip());
        if(getSettings().isLogResultAdoptionsZip()) {
            logResultAdoptionsZip();
        }

        trace("isLogResultAdoptionsZipPhase: {}", getSettings().isLogResultAdoptionsZipPhase());
        if(getSettings().isLogResultAdoptionsZipPhase()) {
            logResultAdoptionsZipPhase();
        }

        trace("isLogResultAdoptionsAll: {}", getSettings().isLogResultAdoptionsAll());
        if(getSettings().isLogResultAdoptionsAll()) {
            logResultAdoptionsAll();
        }
    }

    protected void logResultAdoptionsZip() {
        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsZip analyser = analyseCumulativeAdoptionsZip(true);
        analyser.initCsvPrinterForValueAndCumulativeValue(printer);
        print(InfoTag.RESULT_ZIP_ADOPTIONS, printer, analyser);
    }

    protected void logResultAdoptionsZipPhase() {
        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsZipPhase analyser = new AnnualCumulativeAdoptionsZipPhase();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(getAllSimulationYears());
        analyser.init(getAllZips(analyser.getZipKey()), AdoptionPhase.VALID_PHASES);
        analyser.initCsvPrinterForValueAndCumulativeValue(printer);
        analyser.apply(environment);

        print(InfoTag.RESULT_ZIP_PHASE_ADOPTIONS, printer, analyser);
    }

    protected void logResultAdoptionsAll() {
        ExactAdoptionPrinter printer = new ExactAdoptionPrinter();
        printer.apply(environment);
    }

    protected void print(String infoTag, CsvPrinter<Object> printer, AbstractAdoptionAnalyser analyser) {
        IRPLogging.startResult(infoTag, true);
        IRPLogging.resultWrite(analyser.printHeader(printer));
        analyser.printEntries(printer, IRPLogging::resultWrite);
        IRPLogging.finishResult(infoTag);
        IRPLogging.resultWrite("entries written: {}", analyser.getData().count());
    }

    protected void print(String infoTag, String text) {
        IRPLogging.startResult(infoTag, true);
        IRPLogging.resultWrite(text);
        IRPLogging.finishResult(infoTag);
    }

    //=========================
    //script - wird wohl entfernt
    //=========================

    protected void handleScriptLogging() {
        try {
            handleScriptLogging0();
        } catch (Throwable t) {
            error("error when running 'handleScriptLogging0'", t);
        }
    }

    protected void handleScriptLogging0() {
        trace("isLogScriptAdoptionsZip: {}", getSettings().isLogScriptAdoptionsZip());
        if(getSettings().isLogScriptAdoptionsZip()) {
            logScriptAdoptionsZip();
        }

        trace("isLogScriptAdoptionsZipPhase: {}", getSettings().isLogScriptAdoptionsZipPhase());
        if(getSettings().isLogScriptAdoptionsZipPhase()) {
            logScriptAdoptionsZipPhase();
        }
    }

    protected void logScriptAdoptionsZip() {
        RScriptBuilder builder = RScriptFactory.lineChart0(createBuilderSettingsForZipLineChart(null, getLocalizedImage()));
        builder.setSettings(R_STRING_SETTINGS);
        print(InfoTag.SCRIPT_ZIP_ADOPTIONS, builder.print());

        CsvPrinter<Object> printer = new CsvPrinter<>();
        printer.setDelimiter(defaultSep);
        AnnualCumulativeAdoptionsZip analyser = analyseCumulativeAdoptionsZip(false);
        analyser.initCsvPrinterForValue(printer);

        print(InfoTag.SCRIPT_ZIP_ADOPTIONS_DATA, printer, analyser);
    }

    protected void logScriptAdoptionsZipPhase() {
        RScriptBuilder builder = RScriptFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar(null, getLocalizedImage()));
        builder.setSettings(R_STRING_SETTINGS);
        print(InfoTag.SCRIPT_ZIP_PHASE_ADOPTIONS, builder.print());

        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsPhase analyser = analyseCumulativeAdoptionsPhase(false);
        analyser.initCsvPrinterForCumulativeValue(printer);

        print(InfoTag.SCRIPT_ZIP_PHASE_ADOPTIONS_DATA, printer, analyser);
    }

    //=========================
    //image
    //=========================

    protected void handleImageCreation() {
        try {
            handleImageCreation0();
        } catch (Throwable t) {
            error("error when running 'handleImageCreation0'", t);
        }
    }

    protected void handleImageCreation0() throws ParsingException, IOException {
        if(inRoot.hasImages()) {
            for(InOutputImage image: inRoot.getImages()) {
                trace("image '{}': data={}, script={}, image={}, enabled={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage(), image.isEnabled());
                if(image.isDisabled()) {
                    trace("skip disabled image '{}'", image.getBaseFileName());
                    continue;
                }

                switch (image.getEngine()) {
                    case GNUPLOT:
                        handleGnuPlotImage(image);
                        break;

                    case R:
                        handleRImage(image);
                        break;

                    default:
                        info("unknown engine: '{}'", image.getEngine());
                }
            }
        }
    }

    protected void handleGnuPlotImage(InOutputImage image) throws IOException, ParsingException {
        if(isGnuPlotNotUsable()) {
            info("gnuplot not usable, skip '{}'", image.getBaseFileName());
            return;
        }

        trace("handle '{}': storeData={}, storeScript={}, storeImage={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage());

        GnuPlotBuilder builder = getGnuPlotBuilder(image);
        if(builder == null) {
            return;
        }
        List<List<String>> data = createGnuPlotData(image);
        if(data == null) {
            return;
        }

        builder.setSettings(GNUPLOT_STRING_SETTINGS);
        GnuPlotFileScript fileScript = builder.build();
        GnuPlotImageScriptTask task = new GnuPlotImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(getLocalizedImage().getSep(image.getMode()));
        task.run(
                getGnuPlotEngine(),
                data,
                fileScript
        );
    }

    protected GnuPlotBuilder getGnuPlotBuilder(InOutputImage image) throws ParsingException {
        switch (image.getMode()) {
            case ANNUAL_ZIP:
                return GnuPlotFactory.lineChart0(createBuilderSettingsForZipLineChart(image, getLocalizedImage()));

            case COMPARED_ANNUAL_ZIP:
                return GnuPlotFactory.interactionLineChart0(createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage()));

            case CUMULATIVE_ANNUAL_PHASE:
                return GnuPlotFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar(image, getLocalizedImage()));

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected void handleRImage(InOutputImage image) throws IOException, ParsingException {
        if(isRNotUsable()) {
            info("R not usable, skip '{}'", image.getBaseFileName());
            return;
        }

        trace("handle '{}': storeData={}, storeScript={}, storeImage={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage());

        RScriptBuilder builder = getRScriptBuilder(image);
        if(builder == null) {
            return;
        }
        List<List<String>> data = createRPlotData(image);
        if(data == null) {
            return;
        }

        RFileScript fileScript = builder.build();
        RImageScriptTask task = new RImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(getLocalizedImage().getSep(image.getMode()));
        task.run(
                getRscriptEngine(),
                data,
                fileScript
        );
    }

    protected RScriptBuilder getRScriptBuilder(InOutputImage image) throws ParsingException {
        switch (image.getMode()) {
            case ANNUAL_ZIP:
                return RScriptFactory.lineChart0(createBuilderSettingsForZipLineChart(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            case COMPARED_ANNUAL_ZIP:
                return RScriptFactory.interactionLineChart0(createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            case CUMULATIVE_ANNUAL_PHASE:
                return RScriptFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    //=========================
    //util
    //=========================

    protected Settings getSettings() {
        return environment.getSettings();
    }

    protected Path getTargetDir() throws IOException {
        return clOptions.getCreatedDownloadDir();
    }

    protected List<String> zips;
    protected List<String> getAllZips(String key) {
        if(zips == null) {
            zips = environment.getAgents().streamConsumerAgents()
                    .filter(agent -> agent.hasAnyAttribute(key))
                    .map(agent -> {
                        Attribute attr = agent.findAttribute(key);
                        return attr.asValueAttribute().getValueAsString();
                    })
                    .collect(Collectors.toList());
        }
        return zips;
    }

    protected List<Integer> years;
    protected List<Integer> getAllSimulationYears() {
        if(years == null) {
            int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
            int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
            years = IntStream.rangeClosed(firstYear, lastYear)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return years;
    }

    protected String[] yearBreaks;
    protected String[] getYearBreaksForPrettyR() {
        if(yearBreaks == null) {
            yearBreaks = getAllSimulationYears().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);
        }
        return yearBreaks;
    }

    protected GnuPlotEngine gnuPlotEngine;
    protected GnuPlotEngine getGnuPlotEngine() {
        if(gnuPlotEngine == null) {
            gnuPlotEngine = new GnuPlotEngine(clOptions.getGnuplotCommand());
            debug("use gnuplot engine '{}'", gnuPlotEngine.printCommand());
            debug("gnuplot version: ", gnuPlotEngine.printVersion());
        }
        return gnuPlotEngine;
    }

    protected boolean isGnuPlotUsable() {
        return getGnuPlotEngine().isUsable();
    }
    protected boolean isGnuPlotNotUsable() {
        return !isGnuPlotUsable();
    }

    protected RscriptEngine rscriptEngine;
    protected RscriptEngine getRscriptEngine() {
        if(rscriptEngine == null) {
            rscriptEngine = new RscriptEngine(clOptions.getRscriptCommand());
            debug("use Rscript engine '{}'", rscriptEngine.printCommand());
            debug("R version: {}", rscriptEngine.printVersion());
        }
        return rscriptEngine;
    }

    protected boolean isRUsable() {
        return getRscriptEngine().isUsable();
    }

    protected boolean isRNotUsable() {
        return !isRUsable();
    }

    protected AnnualCumulativeAdoptionsZip analyseCumulativeAdoptionsZip(boolean printBoth) {
        AnnualCumulativeAdoptionsZip analyser = new AnnualCumulativeAdoptionsZip();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(getAllSimulationYears());
        analyser.init(getAllZips(analyser.getZipKey()));
        analyser.setPrintBoth(printBoth);
        analyser.apply(environment);
        return analyser;
    }

    protected AnnualCumulativeAdoptionsPhase analyseCumulativeAdoptionsPhase(boolean printBoth) {
        AnnualCumulativeAdoptionsPhase analyser = new AnnualCumulativeAdoptionsPhase();
        analyser.setYears(getAllSimulationYears());
        analyser.init(AdoptionPhase.NON_INITIAL);
        analyser.setPrintBoth(printBoth);
        for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
            for(AdoptedProduct product: ca.getAdoptedProducts()) {
                if(product.isNotInitial()) {
                    analyser.add(ca, product);
                }
            }
        }
        return analyser;
    }

    protected List<List<String>> createGnuPlotData(InOutputImage image) throws ParsingException {
        switch (image.getMode()) {
            case ANNUAL_ZIP:
                return DataMapper.toGnuPlotData(analyseCumulativeAdoptionsZip(false), DataMapper.GNUPLOT_ESCAPE);

            case COMPARED_ANNUAL_ZIP:
                return DataMapper.toGnuPlotDataWithRealData(analyseCumulativeAdoptionsZip(false), PLACEHOLDER_REAL_DATA, DataMapper.GNUPLOT_ESCAPE);

            case CUMULATIVE_ANNUAL_PHASE:
                return DataMapper.toGnuPlotDataWithCumulativeValue(analyseCumulativeAdoptionsPhase(false), Enum::name, DataMapper.GNUPLOT_ESCAPE);

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected List<List<String>> createRPlotData(InOutputImage image) throws ParsingException {
        switch (image.getMode()) {
            case ANNUAL_ZIP:
                return DataMapper.toRData(
                        analyseCumulativeAdoptionsZip(false),
                        DataMapper.R_ESCAPE);

            case COMPARED_ANNUAL_ZIP:
                BuilderSettings settings = createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage());
                return DataMapper.toRDataWithRealData(
                        analyseCumulativeAdoptionsZip(false),
                        PLACEHOLDER_REAL_DATA,
                        settings.getDistinct0Label(),
                        settings.getDistinct1Label(),
                        DataMapper.R_ESCAPE);

            case CUMULATIVE_ANNUAL_PHASE:
                return DataMapper.toRDataWithCumulativeValue(
                        analyseCumulativeAdoptionsPhase(false),
                        Enum::name,
                        DataMapper.R_ESCAPE);

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected static BuilderSettings createBuilderSettingsForZipLineChart(InOutputImage image, LocalizedImageData localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(DataToVisualize.ANNUAL_ZIP))
                .setXArg(localized.getXArg(DataToVisualize.ANNUAL_ZIP))
                .setXLab(localized.getXLab(DataToVisualize.ANNUAL_ZIP))
                .setYArg(localized.getYArg(DataToVisualize.ANNUAL_ZIP))
                .setYLab(localized.getYLab(DataToVisualize.ANNUAL_ZIP))
                .setGrpArg(localized.getGrpArg(DataToVisualize.ANNUAL_ZIP))
                .setGrpLab(localized.getGrpLab(DataToVisualize.ANNUAL_ZIP))
                .setSep(localized.getSep(DataToVisualize.ANNUAL_ZIP))
                .setLineWidth(image == null ? defaultLinewidth : image.getLinewidth())
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(DataToVisualize.ANNUAL_ZIP))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                //gnuplot
                .setXYRangeWildCard()
                ;
    }

    protected static BuilderSettings createBuilderSettingsForInteractionZipLineChart(InOutputImage image, LocalizedImageData localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setXArg(localized.getXArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setXLab(localized.getXLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setYArg(localized.getYArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setYLab(localized.getYLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setGrpArg(localized.getGrpArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setGrpLab(localized.getGrpLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setDistinctArg(localized.getDistinctArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setDistinctLab(localized.getDistinctLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setDistinct0Label(localized.getDistinct0Lab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setDistinct1Label(localized.getDistinct1Lab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setSep(localized.getSep(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setLineWidth(image == null ? defaultLinewidth : image.getLinewidth())
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(DataToVisualize.COMPARED_ANNUAL_ZIP))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC, Element.CHARACTER)
                //gnuplot
                .setXYRangeWildCard()
                ;
    }

    protected static BuilderSettings createBuilderSettingsForPhaseStackedBar(InOutputImage image, LocalizedImageData localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setXArg(localized.getXArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setXLab(localized.getXLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setYArg(localized.getYArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setYLab(localized.getYLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setFillArg(localized.getFillArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setFillLab(localized.getFillLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setSep(localized.getSep(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setBoxWidthAbsolute(0.8)
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                //gnuplot
                ;
    }

    protected LocalizedImageData getLocalizedImage() {
        if(localizedImageData == null) {
            try {
                if(tryLoadExternal()) {
                    return localizedImageData;
                }
                if(tryLoadInternal()) {
                    return localizedImageData;
                }
                warn("'{}' not found, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            } catch (Exception e) {
                warn("loading '{}' failed, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            }
            localizedImageData = getDefaultLocalizedImage();
        }
        return localizedImageData;
    }

    protected boolean tryLoadExternal() throws IOException {
        if(metaData.getLoader().hasLocalizedExternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedExternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            InputStream in = metaData.getLoader().getLocalizedExternalAsStream(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION);
            return tryLoad(in);
        } else {
            return false;
        }
    }

    protected boolean tryLoadInternal() throws IOException {
        if(metaData.getLoader().hasLocalizedInternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedInternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            InputStream in = metaData.getLoader().getLocalizedInternalAsStream(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION);
            return tryLoad(in);
        } else {
            return false;
        }
    }

    protected boolean tryLoad(InputStream in) throws IOException {
        if(in == null) {
            return false;
        }
        try {
            ObjectNode root = JsonUtil.read(in, JsonUtil.YAML);
            LocalizedImageDataYaml localizedImage = new LocalizedImageDataYaml(metaData.getLocale(), root);
            localizedImage.setEscapeSpecialCharacters(true);
            this.localizedImageData = localizedImage;
            return true;
        } finally {
            in.close();
        }
    }

    protected static LocalizedImageData getDefaultLocalizedImage() {
        LocalizedImageDataYaml data = new LocalizedImageDataYaml(Locale.GERMAN, JsonUtil.YAML.createObjectNode());
        data.setEscapeSpecialCharacters(true);
        //0
        //nichts
        //1
        data.setTitle(DataToVisualize.ANNUAL_ZIP, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete");
        data.setXArg(DataToVisualize.ANNUAL_ZIP, "year");
        data.setYArg(DataToVisualize.ANNUAL_ZIP, "adoptions");
        data.setGrpArg(DataToVisualize.ANNUAL_ZIP, "zip");
        data.setXLab(DataToVisualize.ANNUAL_ZIP, "Jahre");
        data.setYLab(DataToVisualize.ANNUAL_ZIP, "Adoptionen");
        data.setGrpLab(DataToVisualize.ANNUAL_ZIP, "PLZ");
        data.setSep(DataToVisualize.ANNUAL_ZIP, ";");
        data.setEncoding(DataToVisualize.ANNUAL_ZIP, Element.UTF8);
        //2
        data.setTitle(DataToVisualize.COMPARED_ANNUAL_ZIP, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete\nim Vergleich zu realen Daten");
        data.setXArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "year");
        data.setYArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "adoptions");
        data.setGrpArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "zip");
        data.setDistinctArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "real");
        data.setXLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Jahre");
        data.setYLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Adoptionen");
        data.setGrpLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "PLZ");
        data.setDistinctLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Reale Daten");
        data.setDistinct0Lab(DataToVisualize.COMPARED_ANNUAL_ZIP, "nein");
        data.setDistinct1Lab(DataToVisualize.COMPARED_ANNUAL_ZIP, "ja");
        data.setSep(DataToVisualize.COMPARED_ANNUAL_ZIP, ";");
        data.setEncoding(DataToVisualize.COMPARED_ANNUAL_ZIP, Element.UTF8);
        //3
        data.setTitle(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "J\u00e4hrlich kumulierte Adoptionen in Bezug auf die Adoptionsphasen");
        data.setXArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "year");
        data.setYArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "adoptionsCumulative");
        data.setFillArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "phase");
        data.setXLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Jahre");
        data.setYLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Adoptionen (kumuliert)");
        data.setFillLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Adoptionsphase");
        data.setSep(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, ";");
        data.setEncoding(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, Element.UTF8);

        return data;
    }
}
