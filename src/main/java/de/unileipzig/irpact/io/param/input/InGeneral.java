package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
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

        putFieldPathAndAddEntry(res, thisClass(), "logResultGroupedByZip", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);
        putFieldPathAndAddEntry(res, thisClass(), "logResultGroupedByMilieu", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);
        putFieldPathAndAddEntry(res, thisClass(), "logResultGroupedByZipAndMilieu", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);
        putFieldPathAndAddEntry(res, thisClass(), "logProductAdoptions", GENERAL_SETTINGS, LOGGING, LOGGING_RESULT);

        putFieldPathAndAddEntry(res, thisClass(), "runOptActDemo", GENERAL_SETTINGS, SPECIAL_SETTINGS);
        putFieldPathAndAddEntry(res, thisClass(), "runPVAct", GENERAL_SETTINGS, SPECIAL_SETTINGS);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public static final String RUN_OPTACT_DEMO_PARAM_NAME = "sca_InGeneral_runOptActDemo";

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public long timeout;

    //nur fuer interne tests
    public int firstSimulationYear = -1;

    @FieldDefinition
    public int lastSimulationYear;

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
    public boolean logResultGroupedByZip;

    @FieldDefinition
    public boolean logResultGroupedByMilieu;

    @FieldDefinition
    public boolean logResultGroupedByZipAndMilieu;

    @FieldDefinition
    public boolean logProductAdoptions;

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
        copy.firstSimulationYear = firstSimulationYear;
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
        copy.logResultGroupedByZip = logResultGroupedByZip;
        copy.logResultGroupedByMilieu = logResultGroupedByMilieu;
        copy.logResultGroupedByZipAndMilieu = logResultGroupedByZipAndMilieu;
        copy.logProductAdoptions = logProductAdoptions;
        //dev logging
        copy.logSpecificationConverter = logSpecificationConverter;
        copy.logJadexSystemOut = logJadexSystemOut;

        return copy;
    }

    public void setLogLevel(IRPLevel level) {
        this.logLevel = level.getLevelId();
    }

    public void setTimeout(long duration, TimeUnit unit) {
        this.timeout = unit.toMillis(duration);
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
        logResultGroupedByZip = true;
        logResultGroupedByMilieu = true;
        logResultGroupedByZipAndMilieu = true;
        logProductAdoptions = true;
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

        SectionLoggingFilter filter = IRPLogging.getFilter();
        IRPSection.removeAllFrom(filter);
        IRPSection.addAllTo(logAll, filter);
        IRPSection.addAllNonToolsTo(logAllIRPact, filter);
        IRPSection.addAllToolsTo(logAllTools, filter);
        IRPSection.addInitialization(logInitialization, filter);
        IRPSection.addSimulation(logSimulation, filter);

        filter.add(logSpecificationConverter, IRPSection.SPECIFICATION_CONVERTER);
        filter.add(logJadexSystemOut, IRPSection.JADEX_SYSTEM_OUT);

        Settings initData = parser.getEnvironment().getSettings();
        initData.setLogGraphUpdate(logGraphUpdate);
        initData.setLogRelativeAgreement(logRelativeAgreement);
        initData.setLogInterestUpdate(logInterestUpdate);
        initData.setLogShareNetworkLocale(logShareNetworkLocal);
        initData.setLogFinancialComponent(logFinancalComponent);
        initData.setLogCalculateDecisionMaking(logCalculateDecisionMaking);

        initData.setLogResultGroupedByZip(logResultGroupedByZip);
        initData.setLogResultGroupedByMilieu(logResultGroupedByMilieu);
        initData.setLogResultGroupedByZipAndMilieu(logResultGroupedByZipAndMilieu);
        initData.setLogProductAdoptions(logProductAdoptions);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "logging sections: {}", filter.getSections());
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
