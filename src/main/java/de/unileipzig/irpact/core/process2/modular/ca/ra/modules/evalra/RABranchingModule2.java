package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule6_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class RABranchingModule2
        extends AbstractUniformCAMultiModule6_2<RAStage2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RABranchingModule2.class);

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

    protected RAStage2 runInitalization(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule1().apply(input, actions);
    }

    protected RAStage2 runAwareness(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule2().apply(input, actions);
    }

    protected RAStage2 runFeasibility(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule3().apply(input, actions);
    }

    protected RAStage2 runDecisionMaking(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule4().apply(input, actions);
    }

    protected RAStage2 runAdopted(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule5().apply(input, actions);
    }

    protected RAStage2 runImpeded(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule6().apply(input, actions);
    }

    protected RAStage2 initalize(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        RAStage2 nextStage = runInitalization(input, actions);
        return execute(nextStage, input, actions);
    }

    protected RAStage2 execute(RAStage2 stage, ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        switch (stage) {
            case AWARENESS:
                return runAwareness(input, actions);

            case FEASIBILITY:
                return runFeasibility(input, actions);

            case DECISION_MAKING:
                return runDecisionMaking(input, actions);

            case ADOPTED:
                return runAdopted(input, actions);

            case IMPEDED:
                return runImpeded(input, actions);

            default:
                throw new IllegalStateException("unsupported phase: " + stage);
        }
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        RAStage2 stage = getStage(input);
        if(stage == RAStage2.PRE_INITIALIZATION) {
            return initalize(input, actions);
        } else {
            return execute(stage, input, actions);
        }
    }
}
