package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.VoidModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FeasibilityModule2
        extends AbstractUniformCAMultiModule1_2<RAStage2, Void, VoidModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FeasibilityModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    public void setActionModule(VoidModule2<ConsumerAgentData2> module) {
        setSubmodule(module);
    }

    public VoidModule2<ConsumerAgentData2> getActionModule() {
        return getNonnullSubmodule();
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        if(isShareOf1Or2FamilyHouse(input) && isHouseOwner(input)) {
            doSelfActionAndAllowAttention(input);
            return RAStage2.DECISION_MAKING;
        }

        allowAttention(input);
        getActionModule().run(input, actions);
        return RAStage2.FEASIBILITY;
    }
}
