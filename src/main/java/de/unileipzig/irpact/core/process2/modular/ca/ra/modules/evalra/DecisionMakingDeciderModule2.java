package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractGenericCAMultiModuleBase2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.BooleanModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DecisionMakingDeciderModule2
        extends AbstractGenericCAMultiModuleBase2<RAStage2>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DecisionMakingDeciderModule2.class);

    protected BooleanModule2<ConsumerAgentData2> financialCheckModule;
    protected CalculationModule2<ConsumerAgentData2> thresholdModule;
    protected CalculationModule2<ConsumerAgentData2> decisionMakingModule;
    protected boolean forceEvaluation = false;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setThresholdModule(CalculationModule2<ConsumerAgentData2> thresholdModule) {
        this.thresholdModule = thresholdModule;
    }

    public CalculationModule2<ConsumerAgentData2> getThresholdModule() {
        return thresholdModule;
    }

    public void setFinancialCheckModule(BooleanModule2<ConsumerAgentData2> financialCheckModule) {
        this.financialCheckModule = financialCheckModule;
    }

    public BooleanModule2<ConsumerAgentData2> getFinancialCheckModule() {
        return financialCheckModule;
    }

    public void setDecisionMakingModule(CalculationModule2<ConsumerAgentData2> decisionMakingModule) {
        this.decisionMakingModule = decisionMakingModule;
    }

    public CalculationModule2<ConsumerAgentData2> getDecisionMakingModule() {
        return decisionMakingModule;
    }

    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }

    public boolean isForceEvaluation() {
        return forceEvaluation;
    }

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public int getSubmoduleCount() {
        return 3;
    }

    @Override
    public Module2<ConsumerAgentData2, ?> getSubmodule(int index) {
        switch (index) {
            case 0:
                return financialCheckModule;
            case 1:
                return decisionMakingModule;
            case 2:
                return thresholdModule;
            default:
                throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        boolean valid = getFinancialCheckModule().test(input, actions);
        trace("[{}]@[{}] valid={}, isForceEvaluation={}", getName(), input.getAgentName(), valid, isForceEvaluation());

        if(!valid && !isForceEvaluation()) {
            return RAStage2.IMPEDED;
        } else {
            double threshold = getThresholdModule().calculate(input, actions);
            double utility = getDecisionMakingModule().calculate(input, actions);
            trace("[{}]@[{}] threshold={}, utility={}", getName(), input.getAgentName(), threshold, utility);
            if(utility < threshold) {
                return RAStage2.IMPEDED;
            } else {
                trace("[{}]@[{}] store utility={} for product={}", getName(), input.getAgentName(), utility, input.getProductName());
                setUtility(input, utility);
                return RAStage2.ADOPTED;
            }
        }
    }
}
