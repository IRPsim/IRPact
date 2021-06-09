package de.unileipzig.irpact.core.util.result;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.core.util.result.adoptions.*;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.sbuilder.Element;
import de.unileipzig.irpact.util.R.sbuilder.RScriptBuilder;
import de.unileipzig.irpact.util.R.sbuilder.RScriptFactory;
import de.unileipzig.irpact.util.R.sbuilder.StringSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class ResultManager implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ResultManager.class);

    protected static final StringSettings STRING_SETTINGS = new StringSettings();

    protected MetaData metaData;
    protected MainCommandLineOptions clOptions;
    protected SimulationEnvironment environment;

    public ResultManager(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            SimulationEnvironment environment) {
        this.metaData = metaData;
        this.clOptions = clOptions;
        this.environment = environment;
    }

    protected Settings getSettings() {
        return environment.getSettings();
    }

    protected List<Integer> getAllSimulationYears() {
        int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
        int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
        return IntStream.rangeClosed(firstYear, lastYear)
                .boxed()
                .collect(Collectors.toList());
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
    }

    protected void handleResultLogging() {
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

    protected List<String> getAllZips(String key) {
        return environment.getAgents().streamConsumerAgents()
                .filter(agent -> agent.hasAnyAttribute(key))
                .map(agent -> {
                    Attribute attr = agent.findAttribute(key);
                    return attr.asValueAttribute().getValueAsString();
                })
                .collect(Collectors.toList());
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

    protected void logResultAdoptionsZip() {
        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsZip analyser = new AnnualCumulativeAdoptionsZip();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(getAllSimulationYears());
        analyser.init(getAllZips(analyser.getZipKey()));
        analyser.initCsvPrinterForValueAndCumulativeValue(printer);
        analyser.apply(environment);

        print("TEMP0", printer, analyser);
    }

    protected void logResultAdoptionsZipPhase() {
        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsZipPhase analyser = new AnnualCumulativeAdoptionsZipPhase();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(getAllSimulationYears());
        analyser.init(getAllZips(analyser.getZipKey()), AdoptionPhase.VALID_PHASES);
        analyser.initCsvPrinterForValueAndCumulativeValue(printer);
        analyser.apply(environment);

        print("TEMP1", printer, analyser);
    }

    protected void logResultAdoptionsAll() {
        ExactAdoptionPrinter printer = new ExactAdoptionPrinter();
        printer.apply(environment);
    }

    protected void handleScriptLogging() {
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
        RScriptBuilder builder = RScriptFactory.lineChart0(
                "year", "adoptions", "zip",
                9, 6, Element.INCH, 600,
                "Jahre", "Adoptionen", "PLZ"
        );
        builder.setSettings(STRING_SETTINGS);
        print("TEMP3", builder.print());

        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsZip analyser = new AnnualCumulativeAdoptionsZip();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(getAllSimulationYears());
        analyser.init(getAllZips(analyser.getZipKey()));
        analyser.initCsvPrinterForValue(printer);
        analyser.apply(environment);

        print("TEMP4", printer, analyser);
    }

    protected void logScriptAdoptionsZipPhase() {
        RScriptBuilder builder = RScriptFactory.stackedBarChart0(
                "year", "adoptionsCumulative", "phase",
                9, 6, Element.INCH, 600,
                "Jahre", "Adoptionen (kumulativ)", "Adoptionsphase"
        );
        builder.setSettings(STRING_SETTINGS);
        print("TEMP5", builder.print());

        CsvPrinter<Object> printer = new CsvPrinter<>();
        AnnualCumulativeAdoptionsPhase analyser = new AnnualCumulativeAdoptionsPhase();
        analyser.setYears(getAllSimulationYears());
        analyser.init(AdoptionPhase.VALID_PHASES);
        analyser.initCsvPrinterForCumulativeValue(printer);
        analyser.apply(environment);

        print("TEMP6", printer, analyser);
    }
}
