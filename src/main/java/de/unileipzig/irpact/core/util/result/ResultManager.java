package de.unileipzig.irpact.core.util.result;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.core.util.img.*;
import de.unileipzig.irpact.core.util.result.adoptions.*;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.image.InOutputImage;
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
    protected LocalizedImage localizedImage;

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
                if(image.isDisabled()) {
                    trace("skip disabled image '{}'", image.getBaseFileName());
                    continue;
                }

                switch (image.getEngine()) {
                    case InOutputImage.ENGINE_GNUPLOT:
                        handleGnuPlotImage(image);
                        break;

                    case InOutputImage.ENGINE_R:
                        handleRImage(image);
                        break;

                    default:
                        info("unknown engine: '{}'", image.getEngine());
                }
            }
        }
    }

    protected void handleGnuPlotImage(InOutputImage image) throws IOException {
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

    protected GnuPlotBuilder getGnuPlotBuilder(InOutputImage image) {
        switch (image.getMode()) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return GnuPlotFactory.lineChart0(createBuilderSettingsForZipLineChart(image, getLocalizedImage()));

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return GnuPlotFactory.interactionLineChart0(createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage()));

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return GnuPlotFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar(image, getLocalizedImage()));

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", image.getMode());
                return null;

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected void handleRImage(InOutputImage image) throws IOException {
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

    protected RScriptBuilder getRScriptBuilder(InOutputImage image) {
        switch (image.getMode()) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return RScriptFactory.lineChart0(createBuilderSettingsForZipLineChart(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return RScriptFactory.interactionLineChart0(createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return RScriptFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar(image, getLocalizedImage()).setScaleXContinuousBreaks(getYearBreaksForPrettyR()));

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", image.getMode());
                return null;

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
        Path targetDir = clOptions.getDownloadDir();
        if(Files.notExists(targetDir)) {
            Files.createDirectories(targetDir);
        } else {
            if(!Files.isDirectory(targetDir)) {
                throw new IOException("no directoy: " + targetDir);
            }
        }
        return targetDir;
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

    protected List<List<String>> createGnuPlotData(InOutputImage image) {
        switch (image.getMode()) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return DataMapper.toGnuPlotData(analyseCumulativeAdoptionsZip(false), DataMapper.GNUPLOT_ESCAPE);

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return DataMapper.toGnuPlotDataWithRealData(analyseCumulativeAdoptionsZip(false), PLACEHOLDER_REAL_DATA, DataMapper.GNUPLOT_ESCAPE);

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return DataMapper.toGnuPlotDataWithCumulativeValue(analyseCumulativeAdoptionsPhase(false), Enum::name, DataMapper.GNUPLOT_ESCAPE);

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", image.getMode());
                return null;

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected List<List<String>> createRPlotData(InOutputImage image) {
        switch (image.getMode()) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return DataMapper.toRData(
                        analyseCumulativeAdoptionsZip(false),
                        DataMapper.R_ESCAPE);

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                BuilderSettings settings = createBuilderSettingsForInteractionZipLineChart(image, getLocalizedImage());
                return DataMapper.toRDataWithRealData(
                        analyseCumulativeAdoptionsZip(false),
                        PLACEHOLDER_REAL_DATA,
                        settings.getDistinct0Label(),
                        settings.getDistinct1Label(),
                        DataMapper.R_ESCAPE);

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return DataMapper.toRDataWithCumulativeValue(
                        analyseCumulativeAdoptionsPhase(false),
                        Enum::name,
                        DataMapper.R_ESCAPE);

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", image.getMode());
                return null;

            default:
                info("unknown mode: '{}'", image.getMode());
                return null;
        }
    }

    protected static BuilderSettings createBuilderSettingsForZipLineChart(InOutputImage image, LocalizedImage localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(InOutputImage.MODE_ADOPTION_LINECHART))
                .setXArg(localized.getXArg(InOutputImage.MODE_ADOPTION_LINECHART))
                .setXLab(localized.getXLab(InOutputImage.MODE_ADOPTION_LINECHART))
                .setYArg(localized.getYArg(InOutputImage.MODE_ADOPTION_LINECHART))
                .setYLab(localized.getYLab(InOutputImage.MODE_ADOPTION_LINECHART))
                .setGrpArg(localized.getGrpArg(InOutputImage.MODE_ADOPTION_LINECHART))
                .setGrpLab(localized.getGrpLab(InOutputImage.MODE_ADOPTION_LINECHART))
                .setSep(localized.getSep(InOutputImage.MODE_ADOPTION_LINECHART))
                .setLineWidth(image == null ? defaultLinewidth : image.getLinewidth())
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(InOutputImage.MODE_ADOPTION_LINECHART))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                //gnuplot
                .setXYRangeWildCard()
                ;
    }

    protected static BuilderSettings createBuilderSettingsForInteractionZipLineChart(InOutputImage image, LocalizedImage localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setXArg(localized.getXArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setXLab(localized.getXLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setYArg(localized.getYArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setYLab(localized.getYLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setGrpArg(localized.getGrpArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setGrpLab(localized.getGrpLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setDistinctArg(localized.getDistinctArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setDistinctLab(localized.getDistinctLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setDistinct0Label(localized.getDistinct0Lab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setDistinct1Label(localized.getDistinct1Lab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setSep(localized.getSep(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setLineWidth(image == null ? defaultLinewidth : image.getLinewidth())
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC, Element.CHARACTER)
                //gnuplot
                .setXYRangeWildCard()
                ;
    }

    protected static BuilderSettings createBuilderSettingsForPhaseStackedBar(InOutputImage image, LocalizedImage localized) {
        return new BuilderSettings()
                //general
                .setTitle(localized.getTitle(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setXArg(localized.getXArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setXLab(localized.getXLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setYArg(localized.getYArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setYLab(localized.getYLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setFillArg(localized.getFillArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setFillLab(localized.getFillLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setSep(localized.getSep(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setBoxWidthAbsolute(0.8)
                .setUseArgsFlag(true)
                .setUsageFlag(BuilderSettings.USAGE_ARG2)
                .setCenterTitle(true)
                //R
                .setEncoding(localized.getEncoding(InOutputImage.MODE_ADOPTION_PHASE_BARCHART))
                .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                //gnuplot
                ;
    }

    protected LocalizedImage getLocalizedImage() {
        if(localizedImage == null) {
            try {
                if(tryLoadExternal()) {
                    return localizedImage;
                }
                if(tryLoadInternal()) {
                    return localizedImage;
                }
                warn("'{}' not found, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            } catch (Exception e) {
                warn("loading '{}' failed, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            }
            localizedImage = getDefaultLocalizedImage();
        }
        return localizedImage;
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
            ObjectNode root = IRPactJson.read(in, IRPactJson.YAML);
            LocalizedImageYaml localizedImage = new LocalizedImageYaml(metaData.getLocale(), root);
            localizedImage.setEscapeSpecialCharacters(true);
            this.localizedImage = localizedImage;
            return true;
        } finally {
            in.close();
        }
    }

    protected static LocalizedImage getDefaultLocalizedImage() {
        LocalizedImageYaml data = new LocalizedImageYaml(Locale.GERMAN, IRPactJson.YAML.createObjectNode());
        data.setEscapeSpecialCharacters(true);
        //0
        //nichts
        //1
        data.setTitle(InOutputImage.MODE_ADOPTION_LINECHART, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete");
        data.setXArg(InOutputImage.MODE_ADOPTION_LINECHART, "year");
        data.setYArg(InOutputImage.MODE_ADOPTION_LINECHART, "adoptions");
        data.setGrpArg(InOutputImage.MODE_ADOPTION_LINECHART, "zip");
        data.setXLab(InOutputImage.MODE_ADOPTION_LINECHART, "Jahre");
        data.setYLab(InOutputImage.MODE_ADOPTION_LINECHART, "Adoptionen");
        data.setGrpLab(InOutputImage.MODE_ADOPTION_LINECHART, "PLZ");
        data.setSep(InOutputImage.MODE_ADOPTION_LINECHART, ";");
        data.setEncoding(InOutputImage.MODE_ADOPTION_LINECHART, Element.UTF8);
        //2
        data.setTitle(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete\nim Vergleich zu realen Daten");
        data.setXArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "year");
        data.setYArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "adoptions");
        data.setGrpArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "zip");
        data.setDistinctArg(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "real");
        data.setXLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "Jahre");
        data.setYLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "Adoptionen");
        data.setGrpLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "PLZ");
        data.setDistinctLab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "Reale Daten");
        data.setDistinct0Lab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "nein");
        data.setDistinct1Lab(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, "ja");
        data.setSep(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, ";");
        data.setEncoding(InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART, Element.UTF8);
        //3
        data.setTitle(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "J\u00e4hrlich kumulierte Adoptionen in Bezug auf die Adoptionsphasen");
        data.setXArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "year");
        data.setYArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "adoptionsCumulative");
        data.setFillArg(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "phase");
        data.setXLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "Jahre");
        data.setYLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "Adoptionen (kumuliert)");
        data.setFillLab(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, "Adoptionsphase");
        data.setSep(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, ";");
        data.setEncoding(InOutputImage.MODE_ADOPTION_PHASE_BARCHART, Element.UTF8);

        return data;
    }
}
