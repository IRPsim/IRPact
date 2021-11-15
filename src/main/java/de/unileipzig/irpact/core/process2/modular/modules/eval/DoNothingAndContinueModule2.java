package de.unileipzig.irpact.core.process2.modular.modules.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAPlanEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.PlanEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DoNothingAndContinueModule2<I>
        extends AbstractModule2<I, ProcessPlanResult2>
        implements PlanEvaluationModule2<I> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DoNothingAndContinueModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
    }

    @Override
    public void initializeNewInput(I input) throws Throwable {
        traceNewInput(input);
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        traceModuleSetup();
    }

    @Override
    public ProcessPlanResult2 apply(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        return ProcessPlanResult2.CONTINUE;
    }
}
