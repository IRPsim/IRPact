package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.eval;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.PlanEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DoAdoptModule
        extends AbstractUniformCAMultiModule1_2<ProcessPlanResult2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements PlanEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DoAdoptModule.class);

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
    public ProcessPlanResult2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        RAStage2 stage = getNonnullSubmodule().apply(input, actions);
        if(stage == RAStage2.ADOPTED) {
            Timestamp now = input.now();
            adopt(input, now);
        }

        return ProcessPlanResult2.CONTINUE;
    }
}
