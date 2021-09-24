package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.logging.*;
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
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
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

    protected static final String[] timeUnitFieldNames = {"timeoutUseMs", "timeoutUseSec", "timeoutUseMin"};
    protected static final String[] timeUnitFieldNamesWithoutDefault = {"timeoutUseMs", "timeoutUseSec"};
    protected static final XorWithoutUnselectRuleBuilder timeUnitBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(timeUnitFieldNames);

    protected static final String[] logLevelFieldNames = {"levelOff", "levelTrace", "levelDebug", "levelInfo", "levelWarn", "levelError", "levelAll"};
    protected static final String[] logLevelFieldNamesWithoutDefault = {"levelOff", "levelTrace", "levelDebug", "levelWarn", "levelError", "levelAll"};
    protected static final XorWithoutUnselectRuleBuilder logLevelBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(logLevelFieldNames);

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        //general
        putClassPath(res, thisClass(), GENERAL_SETTINGS);

        addEntry(res, thisClass(), "seed");
        addEntry(res, thisClass(), "timeout");
        addEntry(res, thisClass(), "timeoutUseMs");
        addEntry(res, thisClass(), "timeoutUseSec");
        addEntry(res, thisClass(), "timeoutUseMin");
        addEntry(res, thisClass(), "lastSimulationYear");

        setDomain(res, thisClass(), "timout", DOMAIN_GEQ0);
        setDomain(res, thisClass(), timeUnitFieldNames, DOMAIN_BOOLEAN);

        setDefault(res, thisClass(), "seed", varargs(42));
        setDefault(res, thisClass(), "timeout", varargs(1));
        setDefault(res, thisClass(), timeUnitFieldNamesWithoutDefault, VALUE_FALSE);
        setDefault(res, thisClass(), "timeoutUseMin", VALUE_TRUE);

        setRules(res, thisClass(), timeUnitFieldNames, timeUnitBuilder);

        //logging
        putFieldPathAndAddEntry(res, thisClass(), "levelOff", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelTrace", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelDebug", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelInfo", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelWarn", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelError", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "levelAll", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "logAll", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "logAllIRPact", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "logAllTools", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "logInitialization", InRootUI.SETT_GENERAL_LOG);
        putFieldPathAndAddEntry(res, thisClass(), "logSimulation", InRootUI.SETT_GENERAL_LOG);

        setDomain(res, thisClass(), logLevelFieldNames, DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "logAll", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "logAllIRPact", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "logAllTools", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "logInitialization", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "logSimulation", DOMAIN_BOOLEAN);

        setDefault(res, thisClass(), logLevelFieldNamesWithoutDefault, VALUE_FALSE);
        setDefault(res, thisClass(), "levelInfo", VALUE_TRUE);
        setDefault(res, thisClass(), "logAll", VALUE_FALSE);
        setDefault(res, thisClass(), "logAllIRPact", VALUE_FALSE);
        setDefault(res, thisClass(), "logAllTools", VALUE_FALSE);
        setDefault(res, thisClass(), "logInitialization", VALUE_FALSE);
        setDefault(res, thisClass(), "logSimulation", VALUE_FALSE);

        setRules(res, thisClass(), logLevelFieldNames, logLevelBuilder);

        //special
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runOptActDemo", InRootUI.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runPVAct", InRootUI.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runMode", InRootUI.SETT_SPECIAL, VALUE_NEG_ONE, null);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "scenarioMode", InRootUI.SETT_SPECIAL, VALUE_NEG_ONE, null);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "copyLogIfPossible", InRootUI.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "passErrorMessageToOutput", InRootUI.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "printStacktraceImage", InRootUI.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "printNoErrorImage", InRootUI.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "skipPersist", InRootUI.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "forceLogToConsole", InRootUI.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "debugTask", InRootUI.SETT_SPECIAL, VALUE_0, null);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "outerParallelism", InRootUI.SETT_SPECIAL, VALUE_0, DOMAIN_GEQ0);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "innerParallelism", InRootUI.SETT_SPECIAL, VALUE_0, DOMAIN_GEQ0);

        //data
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logResultAdoptionsAll", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logPerformance", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logPhaseOverview", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logInterest", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logEvaluation", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "evaluationBucketSize", InRootUI.SETT_DATAOUTPUT, VALUE_0_1, DOMAIN_GEQ0);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logAllEvaluation", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logFinancialComponent", InRootUI.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);

        //logging general
//        putFieldPathAndAddEntry(res, thisClass(), "logLevel", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);

        //logging script
//        putFieldPathAndAddEntry(res, thisClass(), "logScriptAdoptionsZip", GENERAL_SETTINGS, LOGGING, LOGGING_SCRIPT);
//        putFieldPathAndAddEntry(res, thisClass(), "logScriptAdoptionsZipPhase", GENERAL_SETTINGS, LOGGING, LOGGING_SCRIPT);

    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public static final String SCA_INGENERAL_RUNOPTACTDEMO = "sca_InGeneral_runOptActDemo";

    public static final String SCA_INGENERAL_LOGLEVEL = "sca_InGeneral_logLevel";
    public static final String SCA_INGENERAL_LOGALL = "sca_InGeneral_logAll";
    public static final String SCA_INGENERAL_LOGALLIRPACT = "sca_InGeneral_logAllIRPact";
    public static final String SCA_INGENERAL_LOGALLTOOLS = "sca_InGeneral_logAllTools";
    public static final String SCA_INGENERAL_LOGINITIALIZATION = "sca_InGeneral_logInitialization";
    public static final String SCA_INGENERAL_LOGSIMULATION = "sca_InGeneral_logSimulation";
    public static final String SCA_INGENERAL_FORCELOGTOCONSOLE = "sca_InGeneral_forceLogToConsole";

    public static final String SCA_INGENERAL_PRINTSTACKTRACEIMAGE = "sca_InGeneral_printStacktraceImage";

    @FieldDefinition
    public long seed = 42L;
    public void setSeed(long seed) {
        this.seed = seed;
    }
    public long getSeed() {
        return seed;
    }

    @FieldDefinition
    public long timeout = 1;
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    public long getTimeout() {
        return timeout;
    }
    public void setTimeout(long duration, TimeUnit unit) {
        setTimeout(duration);
        setTimeoutUnit(unit);
    }

    @FieldDefinition
    public boolean timeoutUseMs = false;
    @FieldDefinition
    public boolean timeoutUseSec = false;
    @FieldDefinition
    public boolean timeoutUseMin = true;
    public TimeUnit getTimeoutUnit() throws ParsingException {
        List<TimeUnit> units = new ArrayList<>();
        if(timeoutUseMs) units.add(TimeUnit.MILLISECONDS);
        if(timeoutUseSec) units.add(TimeUnit.SECONDS);
        if(timeoutUseMin) units.add(TimeUnit.MINUTES);
        switch(units.size()) {
            case 0:
                throw new ParsingException("Missing time unit");
            case 1:
                return units.get(0);
            default:
                throw new ParsingException("Multiple time units set: " + units);
        }
    }
    public void setTimeoutUnit(TimeUnit unit) {
        timeoutUseMs = false;
        timeoutUseSec = false;
        timeoutUseMin = false;
        switch (unit) {
            case MILLISECONDS:
                timeoutUseMs = true;
                break;
            case SECONDS:
                timeoutUseSec = true;
                break;
            case MINUTES:
                timeoutUseMin = true;
                break;
            default:
                throw new IllegalArgumentException("unsupported unit: " + unit);
        }
    }

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
    public boolean passErrorMessageToOutput = true;
    public void setPassErrorMessageToOutput(boolean passErrorMessageToOutput) {
        this.passErrorMessageToOutput = passErrorMessageToOutput;
    }
    public boolean shouldPassErrorMessageToOutput() {
        return passErrorMessageToOutput;
    }

    @FieldDefinition
    public boolean printStacktraceImage = true;
    public void setPrintStacktraceImage(boolean printStacktraceImage) {
        this.printStacktraceImage = printStacktraceImage;
    }
    public boolean shouldPrintStacktraceImage() {
        return printStacktraceImage;
    }

    @FieldDefinition
    public boolean printNoErrorImage = true;
    public void setPrintNoErrorImage(boolean printNoErrorImage) {
        this.printNoErrorImage = printNoErrorImage;
    }
    public boolean shouldPrintNoErrorImage() {
        return printNoErrorImage;
    }

    @FieldDefinition
    public boolean copyLogIfPossible = false;
    public void setCopyLogIfPossible(boolean copyLogIfPossible) {
        this.copyLogIfPossible = copyLogIfPossible;
    }
    public boolean doCopyLogIfPossible() {
        return copyLogIfPossible;
    }

    @FieldDefinition
    public boolean skipPersist = false;
    public void setPersistDisabled(boolean disabled) {
        this.skipPersist = disabled;
    }
    public boolean isPersistDisabled() {
        return skipPersist;
    }

    @FieldDefinition
    public boolean forceLogToConsole = false;
    public void setForceLogToConsole(boolean forceLogToConsole) {
        this.forceLogToConsole = forceLogToConsole;
    }
    public boolean isForceLogToConsole() {
        return forceLogToConsole;
    }

    //debug
    @FieldDefinition
    public int debugTask = 0;

    @FieldDefinition
    public int outerParallelism = 0;
    public void setOuterParallelism(int outerParallelism) {
        this.outerParallelism = Math.max(0, outerParallelism);
    }
    public int getOuterParallelism() {
        return outerParallelism;
    }

    @FieldDefinition
    public int innerParallelism = 0;
    public void setInnerParallelism(int innerParallelism) {
        this.innerParallelism = Math.max(0, innerParallelism);
    }
    public int getInnerParallelism() {
        return innerParallelism;
    }

    public int getNumberOfThreads() {
        return getOuterParallelism() * getInnerParallelism();
    }

    //=========================
    //general logging
    //=========================

    @FieldDefinition
    public boolean levelOff;
    @FieldDefinition
    public boolean levelTrace;
    @FieldDefinition
    public boolean levelDebug;
    @FieldDefinition
    public boolean levelInfo;
    @FieldDefinition
    public boolean levelWarn;
    @FieldDefinition
    public boolean levelError;
    @FieldDefinition
    public boolean levelAll;

    public IRPLevel getLogLevel() throws ParsingException {
        List<IRPLevel> levels = new ArrayList<>();
        if(levelOff) levels.add(IRPLevel.OFF);
        if(levelTrace) levels.add(IRPLevel.TRACE);
        if(levelDebug) levels.add(IRPLevel.DEBUG);
        if(levelInfo) levels.add(IRPLevel.INFO);
        if(levelWarn) levels.add(IRPLevel.WARN);
        if(levelError) levels.add(IRPLevel.ERROR);
        if(levelAll) levels.add(IRPLevel.ALL);
        switch(levels.size()) {
            case 0:
                throw new ParsingException("Missing level");
            case 1:
                return levels.get(0);
            default:
                throw new ParsingException("Multiple levels: " + levels);
        }
    }

    public void setLogLevel(IRPLevel level) {
        if(level == null) {
            throw new NullPointerException("level");
        }

        levelOff = false;
        levelTrace = false;
        levelDebug = false;
        levelInfo = false;
        levelWarn = false;
        levelError = false;
        levelAll = false;
        switch (level) {
            case OFF:
                levelOff = true;
                break;
            case TRACE:
                levelTrace = true;
                break;
            case DEBUG:
                levelDebug = true;
                break;
            case INFO:
                levelInfo = true;
                break;
            case WARN:
                levelWarn = true;
                break;
            case ERROR:
                levelError = true;
                break;
            case ALL:
                levelAll = true;
                break;
            default:
                throw new IllegalArgumentException("unsupported level: " + level);
        }
    }

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

    public void useInfoLogging() {
        setLogLevel(IRPLevel.INFO);
    }

    public void doLogAll() {
        setLogLevel(IRPLevel.ALL);
        logAll = true;
    }

    //=========================
    //result logging
    //=========================

    @FieldDefinition
    public boolean logResultAdoptionsAll;

    @FieldDefinition
    public boolean logPerformance;

    @FieldDefinition
    public boolean logPhaseOverview;

    @FieldDefinition
    public boolean logInterest;

    @FieldDefinition
    public boolean logEvaluation;

    @FieldDefinition
    public double evaluationBucketSize = 0.1;
    public void setEvaluationBucketSize(double evaluationBucketSize) {
        this.evaluationBucketSize = evaluationBucketSize;
    }
    public double getEvaluationBucketSize() {
        return evaluationBucketSize;
    }

    @FieldDefinition
    public boolean logAllEvaluation;

    @FieldDefinition
    public boolean logFinancialComponent;

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
        copy.timeoutUseMs = timeoutUseMs;
        copy.timeoutUseSec = timeoutUseSec;
        copy.timeoutUseMin = timeoutUseMin;
        copy.setFirstSimulationYear(getFirstSimulationYear());
        copy.lastSimulationYear = lastSimulationYear;
        //flags
        copy.runPVAct = runPVAct;
        copy.runOptActDemo = runOptActDemo;
        //general logging
        copy.levelOff = levelOff;
        copy.levelTrace = levelTrace;
        copy.levelDebug = levelDebug;
        copy.levelInfo = levelInfo;
        copy.levelWarn = levelWarn;
        copy.levelError = levelError;
        copy.levelAll = levelAll;
        copy.logAll = logAll;
        copy.logAllIRPact = logAllIRPact;
        copy.logAllTools = logAllTools;
        copy.logInitialization = logInitialization;
        copy.logSimulation = logSimulation;
        //data logging
        //result logging
        copy.logResultAdoptionsAll = logResultAdoptionsAll;
        copy.logAllEvaluation = logAllEvaluation;
        copy.logFinancialComponent = logFinancialComponent;
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

    public void enableAllResultLogging() {
        logResultAdoptionsAll = true;
        logPerformance = true;
        logPhaseOverview = true;
        logInterest = true;
        logEvaluation = true;
        logAllEvaluation = true;
        logFinancialComponent = true;
    }

    public void enableAllScriptLogging() {
        logScriptAdoptionsZip = true;
        logScriptAdoptionsZipPhase = true;
    }

    public void parseLoggingSetup(@SuppressWarnings("unused") IRPactInputParser parser) throws ParsingException {
        IRPLogging.setLevel(getLogLevel());

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
        parseDataAnalyserSettings(parser.getEnvironment().getDataAnalyser());
        parseDataLoggerSettings(parser.getEnvironment().getDataLogger());
        parseSeed(parser);
        parseLifeCycleControl(parser);

        ((BasicJadexSimulationEnvironment) parser.getEnvironment()).debugTask = debugTask;
    }

    public void parseSettings(Settings settings) {
        settings.setLogResultAdoptionsAll(logResultAdoptionsAll);
        settings.setLogPerformance(logPerformance);

        settings.setLogScriptAdoptionsZip(logScriptAdoptionsZip);
        settings.setLogScriptAdoptionsZipPhase(logScriptAdoptionsZipPhase);
    }

    public void parseDataAnalyserSettings(DataAnalyser dataAnalyser) {
        dataAnalyser.setLogAnnualInterest(logInterest);
        dataAnalyser.setLogPhaseTransition(logPhaseOverview);
        dataAnalyser.setLogEvaluationData(logEvaluation);
        dataAnalyser.setEvaluationBucketSize(evaluationBucketSize);
    }

    public void parseDataLoggerSettings(DataLogger dataLogger) {
        dataLogger.enableLogEvaluation(logAllEvaluation);
        dataLogger.enableLogFinancialComponent(logFinancialComponent);
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

    private void parseLifeCycleControl(IRPactInputParser parser) throws ParsingException {
        BasicJadexLifeCycleControl lifeCycleControl = (BasicJadexLifeCycleControl) parser.getEnvironment().getLifeCycleControl();
        applyKillSwitch(lifeCycleControl);

        BasicSettings initData = (BasicSettings) parser.getEnvironment().getSettings();
        applyToSettings(initData);
        LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "last simulation year: {}", lastSimulationYear);
    }

    public void applyKillSwitch(BasicJadexLifeCycleControl lifeCycleControl) throws ParsingException {
        if(getTimeout() < 1L) {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout disabled");
        } else {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout: {} {}", getTimeout(), getTimeoutUnit());
        }
        lifeCycleControl.setKillSwitchTimeout(getTimeout(), getTimeoutUnit());
    }

    public void applyToSettings(BasicSettings settings) {
        settings.setLastSimulationYear(lastSimulationYear);
    }
}
