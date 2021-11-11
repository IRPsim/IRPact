package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DoAdoptModule2
        extends AbstractUniformCAMultiModule1_2<RAStage2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DoAdoptModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
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
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        RAStage2 oldStage = input.getStage();
        RAStage2 newStage = getNonnullSubmodule().apply(input, actions);

        boolean adopt;
        if(oldStage == RAStage2.PRE_INITIALIZATION) {
            //'callAopt = true' should never happen, pre-init -> adopt is always initial adopter
            adopt = !isInitialAdopter(input);
            trace("[{}]@[{}] (init) adopt={} ({} -> {})", getName(), input.getAgentName(), adopt, oldStage, newStage);
        } else {
            adopt = oldStage != RAStage2.ADOPTED && newStage == RAStage2.ADOPTED;
            trace("[{}]@[{}] adopt={} ({} -> {})", getName(), input.getAgentName(), adopt, oldStage, newStage);
        }

        if(adopt) {
            if(isAdopter(input)) {
                warn("[{}] agent '{}' has already adopted '{}', skip adoption", getName(), input.getAgentName(), input.getProductName());
            } else {
                Timestamp now = input.now();
                double utility = getUtility(input);
                adopt(input, now, utility);
            }
        }

        return newStage;
    }
}
