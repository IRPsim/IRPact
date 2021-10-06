package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.DataAnalyser;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCARAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class InitializationModule2
        extends AbstractCARAEvaluationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InitializationModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        RAStage2 initialStage;
        if(isAdopter(input)) {
            if(isInitialAdopter(input)) {
                logPhaseTransition(input, DataAnalyser.Phase.INITIAL_ADOPTED, input.now());
            } else {
                logPhaseTransition(input, DataAnalyser.Phase.ADOPTED, input.now());
            }
            initialStage = RAStage2.ADOPTED;
        } else {
            logPhaseTransition(input, DataAnalyser.Phase.AWARENESS, input.now());
            initialStage = RAStage2.AWARENESS;
        }
        updateStage(input, initialStage);
        trace("[{}] initial stage: {}", input.getAgentName(), initialStage);
        return initialStage;
    }
}
