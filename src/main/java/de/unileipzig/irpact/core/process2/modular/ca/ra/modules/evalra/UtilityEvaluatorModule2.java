package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule2_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UtilityEvaluatorModule2
        extends AbstractUniformCAMultiModule2_2<RAStage2, Number, CalculationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UtilityEvaluatorModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setThresholdModule(CalculationModule2<ConsumerAgentData2> thresholdModule) {
        setSubmodule1(thresholdModule);
    }

    public CalculationModule2<ConsumerAgentData2> getThresholdModule() {
        return getNonnullSubmodule1();
    }

    public void setDecisionMakingModule(CalculationModule2<ConsumerAgentData2> decisionMakingModule) {
        setSubmodule2(decisionMakingModule);
    }

    public CalculationModule2<ConsumerAgentData2> getDecisionMakingModule() {
        return getNonnullSubmodule2();
    }

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        double threshold = getThresholdModule().calculate(input, actions);
        double utility = getDecisionMakingModule().calculate(input, actions);
        trace("[{}]@[{}] threshold={}, utility={}", getName(), input.getAgentName(), threshold, utility);
        if(threshold <= utility) {
            return RAStage2.IMPEDED;
        } else {
            return RAStage2.ADOPTED;
        }
    }
}
