package de.unileipzig.irpact.core.process2.modular.modules.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.PlanEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FinishedModule2<I>
        extends AbstractModule2<I, ProcessPlanResult2>
        implements PlanEvaluationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FinishedModule2.class);

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
    public ProcessPlanResult2 apply(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();
        return ProcessPlanResult2.FINISHED;
    }
}
