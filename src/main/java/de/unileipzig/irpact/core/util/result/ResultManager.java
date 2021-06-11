package de.unileipzig.irpact.core.util.result;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
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
import de.unileipzig.irpact.core.util.img.BasicRealAdoptionData;
import de.unileipzig.irpact.core.util.img.DataMapper;
import de.unileipzig.irpact.core.util.img.GnuPlotImageScriptTask;
import de.unileipzig.irpact.core.util.img.RImageScriptTask;
import de.unileipzig.irpact.core.util.result.adoptions.*;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.image.InOutputImage;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.RFileScript;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

    protected String csvDelimiter = ";";
    protected int lineWidth = 1;

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
        RScriptBuilder builder = RScriptFactory.lineChart0(createBuilderSettingsForZipLineChart());
        builder.setSettings(R_STRING_SETTINGS);
        print(InfoTag.SCRIPT_ZIP_ADOPTIONS, builder.print());

        CsvPrinter<Object> printer = new CsvPrinter<>();
        printer.setDelimiter(csvDelimiter);
        AnnualCumulativeAdoptionsZip analyser = analyseCumulativeAdoptionsZip(false);
        analyser.initCsvPrinterForValue(printer);

        print(InfoTag.SCRIPT_ZIP_ADOPTIONS_DATA, printer, analyser);
    }

    protected void logScriptAdoptionsZipPhase() {
        RScriptBuilder builder = RScriptFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar());
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

        GnuPlotBuilder builder = getGnuPlotBuilder(image.getMode());
        if(builder == null) {
            return;
        }
        List<List<String>> data = createGnuPlotData(image.getMode());
        if(data == null) {
            return;
        }

        builder.setSettings(GNUPLOT_STRING_SETTINGS);
        GnuPlotFileScript fileScript = builder.build();
        GnuPlotImageScriptTask task = new GnuPlotImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(csvDelimiter);
        task.run(
                getGnuPlotEngine(),
                data,
                fileScript
        );
    }

    protected GnuPlotBuilder getGnuPlotBuilder(int mode) {
        switch (mode) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return GnuPlotFactory.lineChart1(createBuilderSettingsForZipLineChart());

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return GnuPlotFactory.interactionLineChart0(createBuilderSettingsForInteractionZipLineChart());

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return GnuPlotFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar());

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", mode);
                return null;

            default:
                info("unknown mode: '{}'", mode);
                return null;
        }
    }

    protected void handleRImage(InOutputImage image) throws IOException {
        if(isRNotUsable()) {
            info("R not usable, skip '{}'", image.getBaseFileName());
            return;
        }

        RScriptBuilder builder = getRScriptBuilder(image.getMode());
        if(builder == null) {
            return;
        }
        List<List<String>> data = createRPlotData(image.getMode());
        if(data == null) {
            return;
        }

        RFileScript fileScript = builder.build();
        RImageScriptTask task = new RImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(csvDelimiter);
        task.run(
                getRscriptEngine(),
                data,
                fileScript
        );
    }

    protected RScriptBuilder getRScriptBuilder(int mode) {
        switch (mode) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return RScriptFactory.lineChart0(createBuilderSettingsForZipLineChart());

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return RScriptFactory.interactionLineChart1(createBuilderSettingsForInteractionZipLineChart());

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return RScriptFactory.stackedBarChart0(createBuilderSettingsForPhaseStackedBar());

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", mode);
                return null;

            default:
                info("unknown mode: '{}'", mode);
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

    protected List<List<String>> createGnuPlotData(int mode) {
        switch (mode) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return DataMapper.toGnuPlotData(analyseCumulativeAdoptionsZip(false));

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return DataMapper.toGnuPlotDataWithRealData(analyseCumulativeAdoptionsZip(false), PLACEHOLDER_REAL_DATA);

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return DataMapper.toGnuPlotDataWithCumulativeValue(analyseCumulativeAdoptionsPhase(false), Enum::name);

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", mode);
                return null;

            default:
                info("unknown mode: '{}'", mode);
                return null;
        }
    }

    protected List<List<String>> createRPlotData(int mode) {
        switch (mode) {
            case InOutputImage.MODE_ADOPTION_LINECHART:
                return DataMapper.toRData(analyseCumulativeAdoptionsZip(false));

            case InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART:
                return DataMapper.toRDataWithRealData(analyseCumulativeAdoptionsZip(false), PLACEHOLDER_REAL_DATA);

            case InOutputImage.MODE_ADOPTION_PHASE_BARCHART:
                return DataMapper.toRDataWithCumulativeValue(analyseCumulativeAdoptionsPhase(false), Enum::name);

            case InOutputImage.MODE_NOTHING:
                info("no mode selected ({})", mode);
                return null;

            default:
                info("unknown mode: '{}'", mode);
                return null;
        }
    }

    protected BuilderSettings createBuilderSettingsForZipLineChart() {
        return new BuilderSettings()
                .setXArg("year").setXLab("Jahre")
                .setYArg("adoptions").setYLab("Adoptionen")
                .setGrpArg("zip").setGrpLab("PLZ")
                .setSep(csvDelimiter)
                .setLineWidth(lineWidth)
                .setXYRangeWildCard()
                .setUseArgsFlag(true).setUsageFlag(BuilderSettings.USAGE_ARG2);
    }

    protected BuilderSettings createBuilderSettingsForInteractionZipLineChart() {
        return new BuilderSettings()
                .setXArg("year").setXLab("Jahre")
                .setYArg("adoptions").setYLab("Adoptionen")
                .setGrpArg("zip").setGrpLab("PLZ")
                .setDistinctArg("real").setDistinctLab("Reale Daten")
                .setSep(csvDelimiter)
                .setLineWidth(lineWidth)
                .setXYRangeWildCard()
                .setUseArgsFlag(true).setUsageFlag(BuilderSettings.USAGE_ARG2);
    }

    protected BuilderSettings createBuilderSettingsForPhaseStackedBar() {
        return new BuilderSettings()
                .setXArg("year").setXLab("Jahre")
                .setYArg("adoptionsCumulative").setYLab("Adoptionen (kumuliert)")
                .setFillArg("phase").setFillLab("Adoptionsphasen")
                .setSep(csvDelimiter)
                .setBoxWidthAbsolute(0.8)
                .setUseArgsFlag(true).setUsageFlag(BuilderSettings.USAGE_ARG2);
    }
}
