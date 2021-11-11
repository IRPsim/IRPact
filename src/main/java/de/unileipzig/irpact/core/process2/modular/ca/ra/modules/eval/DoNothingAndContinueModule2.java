package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAPlanEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DoNothingAndContinueModule2
        extends AbstractCAPlanEvaluationModule2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DoNothingAndContinueModule2.class);

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
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    public ProcessPlanResult2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return ProcessPlanResult2.CONTINUE;
    }
}
