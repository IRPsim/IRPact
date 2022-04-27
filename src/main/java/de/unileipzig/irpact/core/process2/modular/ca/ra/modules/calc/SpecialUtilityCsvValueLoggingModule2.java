package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpecialUtilityCsvValueLoggingModule2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpecialUtilityCsvValueLoggingModule2.class);

    public static final Object DATA_ID = new Object();
    public static final int UNSET_ID = -1;
    public static final int UTILTIY_ID = 0;
    public static final int LOCAL_SHARE_ID = 1;
    public static final int SOCIAL_SHARE_ID = 2;
    public static final int ENV_ID = 3;
    public static final int NOV_ID = 4;
    public static final int NPV_ID = 5;

    protected CsvValueLoggingModule2 utilityLogger;
    protected CsvValueLoggingModule2 localShareLogger;
    protected CsvValueLoggingModule2 socialShareLogger;
    protected CsvValueLoggingModule2 envLogger;
    protected CsvValueLoggingModule2 novLogger;
    protected CsvValueLoggingModule2 npvLogger;

    public void setUtilityLogger(CsvValueLoggingModule2 utilityLogger) {
        this.utilityLogger = utilityLogger;
    }

    public void setLocalShareLogger(CsvValueLoggingModule2 localShareLogger) {
        this.localShareLogger = localShareLogger;
    }

    public void setSocialShareLogger(CsvValueLoggingModule2 socialShareLogger) {
        this.socialShareLogger = socialShareLogger;
    }

    public void setEnvLogger(CsvValueLoggingModule2 envLogger) {
        this.envLogger = envLogger;
    }

    public void setNovLogger(CsvValueLoggingModule2 novLogger) {
        this.novLogger = novLogger;
    }

    public void setNpvLogger(CsvValueLoggingModule2 npvLogger) {
        this.npvLogger = npvLogger;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
        if(utilityLogger != null) utilityLogger.validate();
        if(localShareLogger != null) localShareLogger.validate();
        if(socialShareLogger != null) socialShareLogger.validate();
        if(envLogger != null) envLogger.validate();
        if(novLogger != null) novLogger.validate();
        if(npvLogger != null) npvLogger.validate();
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
        if(utilityLogger != null) utilityLogger.initialize(environment);
        if(localShareLogger != null) localShareLogger.initialize(environment);
        if(socialShareLogger != null) socialShareLogger.initialize(environment);
        if(envLogger != null) envLogger.initialize(environment);
        if(novLogger != null) novLogger.initialize(environment);
        if(npvLogger != null) npvLogger.initialize(environment);
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
        if(utilityLogger != null) utilityLogger.initializeNewInput(input);
        if(localShareLogger != null) localShareLogger.initializeNewInput(input);
        if(socialShareLogger != null) socialShareLogger.initializeNewInput(input);
        if(envLogger != null) envLogger.initializeNewInput(input);
        if(novLogger != null) novLogger.initializeNewInput(input);
        if(npvLogger != null) npvLogger.initializeNewInput(input);
    }

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
        if(utilityLogger != null) utilityLogger.setup(environment);
        if(localShareLogger != null) localShareLogger.setup(environment);
        if(socialShareLogger != null) socialShareLogger.setup(environment);
        if(envLogger != null) envLogger.setup(environment);
        if(novLogger != null) novLogger.setup(environment);
        if(npvLogger != null) npvLogger.setup(environment);
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        Data data = new Data();
        input.put(DATA_ID, data);
        double value = getSubmodule().calculate(input, actions);
        input.remove(DATA_ID);

        if(utilityLogger != null && data.isHasUtility()) {
            utilityLogger.tryRunLog(input, data.getUtility());
        }

        if(localShareLogger != null && data.isHasLocalShare()) {
            localShareLogger.tryRunLog(input, data.getLocalShare());
        }

        if(socialShareLogger != null && data.isHasSocialShare()) {
            socialShareLogger.tryRunLog(input, data.getSocialShare());
        }

        if(envLogger != null && data.isHasEnv()) {
            envLogger.tryRunLog(input, data.getEnv());
        }

        if(novLogger != null && data.isHasNov()) {
            novLogger.tryRunLog(input, data.getNov());
        }

        if(npvLogger != null && data.isHasNpv()) {
            npvLogger.tryRunLog(input, data.getNpv());
        }

        return value;
    }

    @Override
    protected void setSharedDataThis(SharedModuleData sharedData) {
        super.setSharedDataThis(sharedData);
        if(utilityLogger != null) utilityLogger.setSharedData(sharedData);
        if(localShareLogger != null) localShareLogger.setSharedData(sharedData);
        if(socialShareLogger != null) socialShareLogger.setSharedData(sharedData);
        if(envLogger != null) envLogger.setSharedData(sharedData);
        if(novLogger != null) novLogger.setSharedData(sharedData);
        if(npvLogger != null) npvLogger.setSharedData(sharedData);
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Data {

        private boolean hasUtility = false;
        private double utility;

        private boolean hasLocalShare = false;
        private double localShare;

        private boolean hasSocialShare = false;
        private double socialShare;

        private boolean hasNpv = false;
        private double npv;

        private boolean hasNov = false;
        private double nov;

        private boolean hasEnv = false;
        private double env;

        public Data() {
        }

        public void setUtility(double utility) {
            hasUtility = true;
            this.utility = utility;
        }

        public double getUtility() {
            return utility;
        }

        public boolean isHasUtility() {
            return hasUtility;
        }

        public void setLocalShare(double localShare) {
            hasLocalShare = true;
            this.localShare = localShare;
        }

        public double getLocalShare() {
            return localShare;
        }

        public boolean isHasLocalShare() {
            return hasLocalShare;
        }

        public void setSocialShare(double socialShare) {
            hasSocialShare = true;
            this.socialShare = socialShare;
        }

        public double getSocialShare() {
            return socialShare;
        }

        public boolean isHasSocialShare() {
            return hasSocialShare;
        }

        public void setNpv(double npv) {
            hasNpv = true;
            this.npv = npv;
        }

        public double getNpv() {
            return npv;
        }

        public boolean isHasNpv() {
            return hasNpv;
        }

        public void setNov(double nov) {
            hasNov = true;
            this.nov = nov;
        }

        public double getNov() {
            return nov;
        }

        public boolean isHasNov() {
            return hasNov;
        }

        public void setEnv(double env) {
            hasEnv = true;
            this.env = env;
        }

        public double getEnv() {
            return env;
        }

        public boolean isHasEnv() {
            return hasEnv;
        }
    }
}
