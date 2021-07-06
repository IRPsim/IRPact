package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.SectionLoggingFilter;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
public class InGeneral implements Copyable {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), GENERAL_SETTINGS);

        addEntry(res, thisClass(), "seed");
        addEntry(res, thisClass(), "timeout");
        addEntry(res, thisClass(), "lastSimulationYear");

        putFieldPathAndAddEntry(res, thisClass(), "logLevel", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logAll", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logAllIRPact", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logAllTools", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logInitialization", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logSimulation", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);

        putFieldPathAndAddEntry(res, thisClass(), "logGraphUpdate", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);
        putFieldPathAndAddEntry(res, thisClass(), "logRelativeAgreement", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);
        putFieldPathAndAddEntry(res, thisClass(), "logInterestUpdate", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);
        putFieldPathAndAddEntry(res, thisClass(), "logShareNetworkLocal", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);
        putFieldPathAndAddEntry(res, thisClass(), "logFinancalComponent", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);
        putFieldPathAndAddEntry(res, thisClass(), "logCalculateDecisionMaking", GENERAL_SETTINGS, LOGGING, LOGGING_DATA);

        putFieldPathAndAddEntry(res, thisClass(), "logResultAdoptionsZip", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);
        putFieldPathAndAddEntry(res, thisClass(), "logResultAdoptionsZipPhase", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);
        putFieldPathAndAddEntry(res, thisClass(), "logResultAdoptionsAll", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);

        putFieldPathAndAddEntry(res, thisClass(), "logScriptAdoptionsZip", GENERAL_SETTINGS, LOGGING, LOGGING_SCRIPT);
        putFieldPathAndAddEntry(res, thisClass(), "logScriptAdoptionsZipPhase", GENERAL_SETTINGS, LOGGING, LOGGING_SCRIPT);

        putFieldPathAndAddEntry(res, thisClass(), "runOptActDemo", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "runPVAct", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "runMode", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "scenarioMode", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "copyLogIfPossible", GENERAL_SETTINGS, SPECIAL_SETTINGS);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public static final String SCA_INGENERAL_RUNOPTACTDEMO = "sca_InGeneral_runOptActDemo";

    public static final String SCA_INGENERAL_LOGLEVEL = "sca_InGeneral_logLevel";
    public static final String SCA_INGENERAL_LOGALL = "sca_InGeneral_logAll";
    public static final String SCA_INGENERAL_LOGALLIRPACT = "sca_InGeneral_logAllIRPact";
    public static final String SCA_INGENERAL_LOGALLTOOLS = "sca_InGeneral_logAllTools";
    public static final String SCA_INGENERAL_LOGINITIALIZATION = "sca_InGeneral_logInitialization";
    public static final String SCA_INGENERAL_LOGSIMULATION = "sca_InGeneral_logSimulation";

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public long timeout;

    //internal only
    private final MutableInt firstSimulationYear0 = MutableInt.empty();
    public boolean hasFirstSimulationYear() {
        return firstSimulationYear0.hasValue();
    }
    public int getFirstSimulationYear() {
        return firstSimulationYear0.get();
    }
    public void setFirstSimulationYear(int year) {
        firstSimulationYear0.set(year);
    }

    @FieldDefinition
    public int lastSimulationYear;
    public void setFirstSimulationYearAsLast() {
        lastSimulationYear = getFirstSimulationYear();
    }
    public void setLastSimulationYear(int lastSimulationYear) {
        this.lastSimulationYear = lastSimulationYear;
    }
    public int getLastSimulationYear() {
        return lastSimulationYear;
    }

    //=========================
    //IRPopt
    //=========================

    @FieldDefinition(
            name = "a",
            gams = @GamsParameter(
                    description = "Einlesen des zu optimierenden Jahres",
                    identifier = "Jahreszahl",
                    hidden = Constants.TRUE1
            )
    )
    public int a;

    @FieldDefinition(
            name = "delta_ii",
            gams = @GamsParameter(
                    description = "Einlesen der Zeitschrittlänge der Simulationszeitreihen (bezogen auf eine Stunde Bsp. 15 Min = 0.25)",
                    identifier = "Zeitschrittl\u00e4nge",
                    domain = "(0,)",
                    defaultValue = "0.25",
                    hidden = Constants.TRUE1
            )
    )
    public double deltaii;

    //=========================
    //special
    //=========================

    @FieldDefinition
    public boolean runPVAct;

    @FieldDefinition
    public boolean runOptActDemo;

    @FieldDefinition
    public int runMode = -1;

    @FieldDefinition
    public int scenarioMode = -1;

    @FieldDefinition
    public boolean copyLogIfPossible = false;
    public void setCopyLogIfPossible(boolean copyLogIfPossible) {
        this.copyLogIfPossible = copyLogIfPossible;
    }
    public boolean isCopyLogIfPossible() {
        return copyLogIfPossible;
    }

    //=========================
    //general logging
    //=========================

    @FieldDefinition
    public int logLevel;

    @FieldDefinition
    public boolean logAll;

    @FieldDefinition
    public boolean logAllIRPact;

    @FieldDefinition
    public boolean logAllTools;

    @FieldDefinition
    public boolean logInitialization;

    @FieldDefinition
    public boolean logSimulation;

    //=========================
    //data logging
    //=========================

    @FieldDefinition
    public boolean logGraphUpdate;

    @FieldDefinition
    public boolean logRelativeAgreement;

    @FieldDefinition
    public boolean logInterestUpdate;

    @FieldDefinition
    public boolean logShareNetworkLocal;

    @FieldDefinition
    public boolean logFinancalComponent;

    @FieldDefinition
    public boolean logCalculateDecisionMaking;

    //=========================
    //result logging
    //=========================

    @FieldDefinition
    public boolean logResultAdoptionsZip;

    @FieldDefinition
    public boolean logResultAdoptionsZipPhase;

    @FieldDefinition
    public boolean logResultAdoptionsAll;

    //=========================
    //script + data logging
    //=========================

    @FieldDefinition
    public boolean logScriptAdoptionsZip;

    @FieldDefinition
    public boolean logScriptAdoptionsZipPhase;

    //=========================
    //dev logging
    //=========================

    public boolean logSpecificationConverter;

    public boolean logJadexSystemOut;

    //=========================
    //...
    //=========================

    public InGeneral() {
    }

    @Override
    public InGeneral copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGeneral newCopy(CopyCache cache) {
        InGeneral copy = new InGeneral();
        copy.seed = seed;
        copy.timeout = timeout;
        copy.setFirstSimulationYear(getFirstSimulationYear());
        copy.lastSimulationYear = lastSimulationYear;
        //flags
        copy.runPVAct = runPVAct;
        copy.runOptActDemo = runOptActDemo;
        //general logging
        copy.logLevel = logLevel;
        copy.logAll = logAll;
        copy.logAllIRPact = logAllIRPact;
        copy.logAllTools = logAllTools;
        copy.logInitialization = logInitialization;
        copy.logSimulation = logSimulation;
        //data logging
        copy.logGraphUpdate = logGraphUpdate;
        copy.logRelativeAgreement = logRelativeAgreement;
        copy.logInterestUpdate = logInterestUpdate;
        copy.logShareNetworkLocal = logShareNetworkLocal;
        copy.logFinancalComponent = logFinancalComponent;
        copy.logCalculateDecisionMaking = logCalculateDecisionMaking;
        //result logging
        copy.logResultAdoptionsZip = logResultAdoptionsZip;
        copy.logResultAdoptionsZipPhase = logResultAdoptionsZipPhase;
        copy.logResultAdoptionsAll = logResultAdoptionsAll;
        //script + data logging
        copy.logScriptAdoptionsZip = logScriptAdoptionsZip;
        copy.logScriptAdoptionsZipPhase = logScriptAdoptionsZipPhase;
        //dev logging
        copy.logSpecificationConverter = logSpecificationConverter;
        copy.logJadexSystemOut = logJadexSystemOut;

        return copy;
    }

    public void setRunMode(int runMode) {
        this.runMode = runMode;
    }

    public int getRunMode() {
        return runMode;
    }

    public void setScenarioMode(int scenarioMode) {
        this.scenarioMode = scenarioMode;
    }

    public int getScenarioMode() {
        return scenarioMode;
    }

    public void setLogLevel(IRPLevel level) {
        this.logLevel = level.getLevelId();
    }

    public void setTimeout(long duration, TimeUnit unit) {
        this.timeout = unit.toMillis(duration);
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void enableAllDataLogging() {
        logGraphUpdate = true;
        logRelativeAgreement = true;
        logInterestUpdate = true;
        logShareNetworkLocal = true;
        logFinancalComponent = true;
        logCalculateDecisionMaking = true;
    }

    public void enableAllResultLogging() {
        logResultAdoptionsZip = true;
        logResultAdoptionsZipPhase = true;
        logResultAdoptionsAll = true;
    }

    public void enableAllScriptLogging() {
        logScriptAdoptionsZip = true;
        logScriptAdoptionsZipPhase = true;
    }

    public void parseLoggingSetup(@SuppressWarnings("unused") IRPactInputParser parser) {

        IRPLevel level = IRPLevel.get(logLevel);
        if(level == null) {
            LOGGER.warn("invalid log level {}, set level to default ({}) ", logLevel, IRPLevel.getDefault());
            level = IRPLevel.getDefault();
        }
        IRPLogging.setLevel(level);

        SectionLoggingFilter filter = IRPLogging.getFilter();
        IRPSection.removeAllFrom(filter);
        IRPSection.addAllTo(logAll, filter);
        IRPSection.addAllNonToolsTo(logAllIRPact, filter);
        IRPSection.addAllToolsTo(logAllTools, filter);
        IRPSection.addInitialization(logInitialization, filter);
        IRPSection.addSimulation(logSimulation, filter);

        filter.add(logSpecificationConverter, IRPSection.SPECIFICATION_CONVERTER);
        filter.add(logJadexSystemOut, IRPSection.JADEX_SYSTEM_OUT);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "logging sections: {}", filter.getSections());
    }

    public void setup(IRPactInputParser parser) throws ParsingException {
        parseSettings(parser.getEnvironment().getSettings());
        parseSeed(parser);
        parseLifeCycleControl(parser);
    }

    public void parseSettings(Settings settings) {
        settings.setLogGraphUpdate(logGraphUpdate);
        settings.setLogRelativeAgreement(logRelativeAgreement);
        settings.setLogInterestUpdate(logInterestUpdate);
        settings.setLogShareNetworkLocale(logShareNetworkLocal);
        settings.setLogFinancialComponent(logFinancalComponent);
        settings.setLogCalculateDecisionMaking(logCalculateDecisionMaking);

        settings.setLogResultAdoptionsZip(logResultAdoptionsZip);
        settings.setLogResultAdoptionsZipPhase(logResultAdoptionsZipPhase);
        settings.setLogResultAdoptionsAll(logResultAdoptionsAll);

        settings.setLogScriptAdoptionsZip(logScriptAdoptionsZip);
        settings.setLogScriptAdoptionsZipPhase(logScriptAdoptionsZipPhase);
    }

    private void parseSeed(IRPactInputParser parser) {
        BasicJadexSimulationEnvironment env = (BasicJadexSimulationEnvironment) parser.getEnvironment();
        final Rnd rnd;
        if(seed == -1L) {
            rnd = new Rnd();
            LOGGER.info("use random master seed: {}", rnd.getInitialSeed());
        } else {
            LOGGER.info("use master seed: {}", seed);
            rnd = new Rnd(seed);
        }
        env.setSimulationRandom(rnd);
    }

    private void parseLifeCycleControl(IRPactInputParser parser) {
        BasicJadexLifeCycleControl lifeCycleControl = (BasicJadexLifeCycleControl) parser.getEnvironment().getLiveCycleControl();
        applyKillSwitch(lifeCycleControl);

        BasicSettings initData = (BasicSettings) parser.getEnvironment().getSettings();
        applyToSettings(initData);
        LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "last simulation year: {}", lastSimulationYear);
    }

    public void applyKillSwitch(BasicJadexLifeCycleControl lifeCycleControl) {
        if(timeout < 1L) {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout disabled");
        } else {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout: {}", timeout);
        }
        lifeCycleControl.setKillSwitchTimeout(timeout);
    }

    public void applyToSettings(BasicSettings settings) {
        settings.setLastSimulationYear(lastSimulationYear);
    }
}
