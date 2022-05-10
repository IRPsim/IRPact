package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.logging.*;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.*;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
@LocalizedUiResource.XorWithoutUnselectRule(InGeneral.TIME)
@LocalizedUiResource.XorWithoutUnselectRule(InGeneral.LOG_LEVEL)
public class InGeneral implements Copyable {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    static final String TIME = "TIME";
    static final String LOG_LEVEL = "LOG_LEVEL";

//    protected static final String[] timeUnitFieldNames = {"timeoutUseMs", "timeoutUseSec", "timeoutUseMin"};
//    protected static final String[] timeUnitFieldNamesWithoutDefault = {"timeoutUseMs", "timeoutUseSec"};
//    protected static final XorWithoutUnselectRuleBuilder timeUnitBuilder = new XorWithoutUnselectRuleBuilder()
//            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
//            .withTrueValue(Constants.TRUE1)
//            .withFalseValue(Constants.FALSE0)
//            .withKeys(timeUnitFieldNames);

//    protected static final String[] logLevelFieldNames = {"levelOff", "levelTrace", "levelDebug", "levelInfo", "levelWarn", "levelError", "levelAll"};
//    protected static final String[] logLevelFieldNamesWithoutDefault = {"levelOff", "levelTrace", "levelDebug", "levelWarn", "levelError", "levelAll"};
//    protected static final XorWithoutUnselectRuleBuilder logLevelBuilder = new XorWithoutUnselectRuleBuilder()
//            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
//            .withTrueValue(Constants.TRUE1)
//            .withFalseValue(Constants.FALSE0)
//            .withKeys(logLevelFieldNames);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
        //general
//        putClassPath(res, thisClass(), GENERAL_SETTINGS);
//
//        addEntry(res, thisClass(), "seed");
//        addEntry(res, thisClass(), "timeout");
//        addEntry(res, thisClass(), "timeoutUseMs");
//        addEntry(res, thisClass(), "timeoutUseSec");
//        addEntry(res, thisClass(), "timeoutUseMin");
//        addEntry(res, thisClass(), "lastSimulationYear");
//
//        setDomain(res, thisClass(), "timout", DOMAIN_GEQ0);
//        setDomain(res, thisClass(), timeUnitFieldNames, DOMAIN_BOOLEAN);
//
//        setDefault(res, thisClass(), "seed", varargs(42));
//        setDefault(res, thisClass(), "timeout", varargs(1));
//        setDefault(res, thisClass(), timeUnitFieldNamesWithoutDefault, VALUE_FALSE);
//        setDefault(res, thisClass(), "timeoutUseMin", VALUE_TRUE);
//
//        setRules(res, thisClass(), timeUnitFieldNames, timeUnitBuilder);
//
//        //logging
//        putFieldPathAndAddEntry(res, thisClass(), "levelOff", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelTrace", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelDebug", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelInfo", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelWarn", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelError", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "levelAll", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "logAll", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "logAllIRPact", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "logAllTools", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "logInitialization", TreeViewStructure.SETT_GENERAL_LOG);
//        putFieldPathAndAddEntry(res, thisClass(), "logSimulation", TreeViewStructure.SETT_GENERAL_LOG);
//
//        setDomain(res, thisClass(), logLevelFieldNames, DOMAIN_BOOLEAN);
//        setDomain(res, thisClass(), "logAll", DOMAIN_BOOLEAN);
//        setDomain(res, thisClass(), "logAllIRPact", DOMAIN_BOOLEAN);
//        setDomain(res, thisClass(), "logAllTools", DOMAIN_BOOLEAN);
//        setDomain(res, thisClass(), "logInitialization", DOMAIN_BOOLEAN);
//        setDomain(res, thisClass(), "logSimulation", DOMAIN_BOOLEAN);
//
//        setDefault(res, thisClass(), logLevelFieldNamesWithoutDefault, VALUE_FALSE);
//        setDefault(res, thisClass(), "levelInfo", VALUE_TRUE);
//        setDefault(res, thisClass(), "logAll", VALUE_FALSE);
//        setDefault(res, thisClass(), "logAllIRPact", VALUE_FALSE);
//        setDefault(res, thisClass(), "logAllTools", VALUE_FALSE);
//        setDefault(res, thisClass(), "logInitialization", VALUE_FALSE);
//        setDefault(res, thisClass(), "logSimulation", VALUE_FALSE);
//
//        setRules(res, thisClass(), logLevelFieldNames, logLevelBuilder);
//
//        //special
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runOptActDemo", TreeViewStructure.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runPVAct", TreeViewStructure.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "runMode", TreeViewStructure.SETT_SPECIAL, VALUE_NEG_ONE, null);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "scenarioMode", TreeViewStructure.SETT_SPECIAL, VALUE_NEG_ONE, null);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "copyLogIfPossible", TreeViewStructure.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "passErrorMessageToOutput", TreeViewStructure.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "printStacktraceImage", TreeViewStructure.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "printNoErrorImage", TreeViewStructure.SETT_SPECIAL, VALUE_TRUE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "skipPersist", TreeViewStructure.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "forceLogToConsole", TreeViewStructure.SETT_SPECIAL, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "debugTask", TreeViewStructure.SETT_SPECIAL, VALUE_0, null);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "outerParallelism", TreeViewStructure.SETT_SPECIAL, VALUE_0, DOMAIN_GEQ0);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "innerParallelism", TreeViewStructure.SETT_SPECIAL, VALUE_0, DOMAIN_GEQ0);
//
//        //data
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logResultAdoptionsAll", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logPerformance", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logPhaseOverview", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logInterest", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logEvaluation", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "evaluationBucketSize", TreeViewStructure.SETT_DATAOUTPUT, VALUE_0_1, DOMAIN_GEQ0);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logAllEvaluation", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);
//        putFieldPathAndAddEntryWithDefaultAndDomain(res, thisClass(), "logFinancialComponent", TreeViewStructure.SETT_DATAOUTPUT, VALUE_FALSE, DOMAIN_BOOLEAN);

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
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = 42
    )
    public long seed = 42L;
    public void setSeed(long seed) {
        this.seed = seed;
    }
    public long getSeed() {
        return seed;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = 5
    )
    public long timeout = 5;
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
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(TIME)
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean timeoutUseMs = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(TIME)
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean timeoutUseSec = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(TIME)
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
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

    @FieldDefinition
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean enableFirstSimulationYear = false;
    public void setEnableFirstSimulationYear(boolean enableFirstSimulationYear) {
        this.enableFirstSimulationYear = enableFirstSimulationYear;
    }
    public boolean isEnableFirstSimulationYear() {
        return enableFirstSimulationYear;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int firstSimulationYear = 0;
    public int getFirstSimulationYear() {
        return firstSimulationYear;
    }
    public void setFirstSimulationYear(int year) {
        this.firstSimulationYear = year;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(TreeViewStructureEnum.SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int lastSimulationYear = 0;
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
            name = "a"
    )
    @LocalizedUiResource.AddEntry(SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            hidden = true
    )
    public int a;

    @FieldDefinition(
            name = "delta_ii"
    )
    @LocalizedUiResource.AddEntry(SETT_GENERAL)
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            decDefault = 0.25,
            hidden = true
    )
    public double deltaii;

    //=========================
    //special
    //=========================

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean runPVAct = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean runOptActDemo = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = -1
    )
    public int runMode = -1;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = -1
    )
    public int scenarioMode = -1;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean passErrorMessageToOutput = true;
    public void setPassErrorMessageToOutput(boolean passErrorMessageToOutput) {
        this.passErrorMessageToOutput = passErrorMessageToOutput;
    }
    public boolean shouldPassErrorMessageToOutput() {
        return passErrorMessageToOutput;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean printStacktraceImage = true;
    public void setPrintStacktraceImage(boolean printStacktraceImage) {
        this.printStacktraceImage = printStacktraceImage;
    }
    public boolean shouldPrintStacktraceImage() {
        return printStacktraceImage;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean printNonErrorImage = true;
    public void setPrintNonErrorImage(boolean printNonErrorImage) {
        this.printNonErrorImage = printNonErrorImage;
    }
    public boolean shouldPrintNoErrorImage() {
        return printNonErrorImage;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean copyLogIfPossible = false;
    public void setCopyLogIfPossible(boolean copyLogIfPossible) {
        this.copyLogIfPossible = copyLogIfPossible;
    }
    public boolean doCopyLogIfPossible() {
        return copyLogIfPossible;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean skipPersist = false;
    public void setPersistDisabled(boolean disabled) {
        this.skipPersist = disabled;
    }
    public boolean isPersistDisabled() {
        return skipPersist;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean forceLogToConsole = false;
    public void setForceLogToConsole(boolean forceLogToConsole) {
        this.forceLogToConsole = forceLogToConsole;
    }
    public boolean isForceLogToConsole() {
        return forceLogToConsole;
    }

    //debug
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int debugTask = 0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            geq0Domain = true,
            intDefault = 0
    )
    public int outerParallelism = 0;
    public void setOuterParallelism(int outerParallelism) {
        this.outerParallelism = Math.max(0, outerParallelism);
    }
    public int getOuterParallelism() {
        return outerParallelism;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_SPECIAL)
    @LocalizedUiResource.SimpleSet(
            geq0Domain = true,
            intDefault = 0
    )
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
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelOff = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelTrace = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelDebug = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean levelInfo = true;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelWarn = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelError = false;
    @FieldDefinition
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(LOG_LEVEL)
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean levelAll = false;

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
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logAll = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logAllIRPact = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logAllTools = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logInitialization = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_GENERAL_LOG)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logSimulation = false;

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
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logResultAdoptionsAll = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logAdoptionAnalysis = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logPerformance = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logPhaseOverview = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logInterest = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean logEvaluation = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SETT_DATAOUTPUT)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0.1
    )
    public double evaluationBucketSize = 0.1;
    public void setEvaluationBucketSize(double evaluationBucketSize) {
        this.evaluationBucketSize = evaluationBucketSize;
    }
    public double getEvaluationBucketSize() {
        return evaluationBucketSize;
    }

    //=========================
    //script + data logging
    //=========================

    public boolean logScriptAdoptionsZip = false;

    public boolean logScriptAdoptionsZipPhase = false;

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
        logAdoptionAnalysis = true;
        logPerformance = true;
        logPhaseOverview = true;
        logInterest = true;
        logEvaluation = true;
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
        parseSeed(parser);
        parseLifeCycleControl(parser);

        ((BasicJadexSimulationEnvironment) parser.getEnvironment()).debugTask = debugTask;
    }

    public void parseSettings(Settings settings) {
        settings.setLogResultAdoptionsAll(logResultAdoptionsAll);
        settings.setLogAdoptionAnalysis(logAdoptionAnalysis);
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
