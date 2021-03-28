package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.core.simulation.BasicInitializationData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class InGeneral {

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
        addEntry(res, thisClass(), "endYear");

        putFieldPathAndAddEntry(res, thisClass(), "logLevel", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logAll", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);
        putFieldPathAndAddEntry(res, thisClass(), "logAllTools", GENERAL_SETTINGS, LOGGING, LOGGING_GENERAL);

        putFieldPathAndAddEntry(res, thisClass(), "logToolsCore", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logToolsDefinition", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logInitialization", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logParamInit", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logGraphCreation", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logAgentCreation", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logPlatformCreation", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logSimulationLifecycle", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logSimulationAgent", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);
        putFieldPathAndAddEntry(res, thisClass(), "logJadexSystemOut", GENERAL_SETTINGS, LOGGING, LOGGING_SPECIFIC);

        putFieldPathAndAddEntry(res, thisClass(), "runOptActDemo", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "runPVAct", GENERAL_SETTINGS, SPECIAL_SETTINGS);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public static final String RUN_OPTACT_DEMO_PARAM_NAME = "sca_InGeneral_runOptActDemo";

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public long timeout;

    //fuer den anderen Spec
    public int startYear = -1;

    @FieldDefinition
    public int endYear;

    //=========================
    //flags
    //=========================

    @FieldDefinition
    public boolean runPVAct;

    @FieldDefinition
    public boolean runOptActDemo;

    //=========================
    //general logging
    //=========================

    @FieldDefinition
    public int logLevel;

    @FieldDefinition
    public boolean logAll;

    @FieldDefinition
    public boolean logAllTools;

    //=========================
    //specific logging
    //=========================
    @FieldDefinition
    public boolean logToolsCore;
    @FieldDefinition
    public boolean logToolsDefinition;

    @FieldDefinition
    public boolean logInitialization;
    @FieldDefinition
    public boolean logParamInit;
    @FieldDefinition
    public boolean logGraphCreation;
    @FieldDefinition
    public boolean logAgentCreation;
    @FieldDefinition
    public boolean logPlatformCreation;

    @FieldDefinition
    public boolean logSimulationLifecycle;
    @FieldDefinition
    public boolean logSimulationAgent;

    @FieldDefinition
    public boolean logJadexSystemOut;

    //=========================
    //...
    //=========================

    public InGeneral() {
    }

    public void setLogLevel(IRPLevel level) {
        this.logLevel = level.getLevelId();
    }

    public void setTimeout(long duration, TimeUnit unit) {
        this.timeout = unit.toMillis(duration);
    }

    public void setup(InputParser parser) throws ParsingException {
        parseLoggingSetup(parser);
        parseSeed(parser);
        parseLifeCycleControl(parser);
    }

    private void parseLoggingSetup(@SuppressWarnings("unused") InputParser parser) {
        IRPLevel level = IRPLevel.get(logLevel);
        if(level == null) {
            LOGGER.warn("invalid log level {}, set level to default ({}) ", logLevel, IRPLevel.getDefault());
            level = IRPLevel.getDefault();
        }
        IRPLogging.setLevel(level);

        SectionLoggingFilter filter = (SectionLoggingFilter) IRPLogging.getFilter();
        IRPSection.removeAllFrom(filter);

        if(logAll) {
            IRPSection.addAllNonToolsTo(filter);
            if(logAllTools) IRPSection.addAllToolsTo(filter);
            return;
        }

        if(logAllTools) {
            IRPSection.addAllToolsTo(filter);
        } else {
            if(logToolsCore) filter.add(IRPSection.TOOLS_CORE);
            if(logToolsDefinition) filter.add(IRPSection.TOOLS_DEFINITION);
        }

        if(logParamInit) filter.add(IRPSection.INITIALIZATION_PARAMETER);
        if(logAgentCreation) filter.add(IRPSection.INITIALIZATION_AGENT);
        if(logGraphCreation) filter.add(IRPSection.INITIALIZATION_NETWORK);
        if(logPlatformCreation) filter.add(IRPSection.INITIALIZATION_PLATFORM);

        if(logSimulationLifecycle) filter.add(IRPSection.SIMULATION_LICECYCLE);
        if(logSimulationAgent) filter.add(IRPSection.SIMULATION_AGENT);

        if(logJadexSystemOut) filter.add(IRPSection.JADEX_SYSTEM_OUT);
    }

    private void parseSeed(InputParser parser) {
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

    private void parseLifeCycleControl(InputParser parser) {
        BasicJadexLifeCycleControl lifeCycleControl = (BasicJadexLifeCycleControl) parser.getEnvironment().getLiveCycleControl();
        BasicInitializationData initData = (BasicInitializationData) parser.getEnvironment().getInitializationData();
        if(timeout < 1L) {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout disabled");
        } else {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout: {}", timeout);
        }
        lifeCycleControl.setKillSwitchTimeout(timeout);

        initData.setStartYear(startYear);
        initData.setEndYear(endYear);
        LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "custom endyear: {}", endYear);
    }
}
