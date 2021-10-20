package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.bool;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.BooleanModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class IfThresholdModule2
        extends AbstractUniformCAMultiModule1_2<Boolean, Number, CalculationModule2<ConsumerAgentData2>>
        implements BooleanModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IfThresholdModule2.class);

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
    public boolean test(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();

        double draw = input.rnd().nextDouble();
        double threshold = getNonnullSubmodule().calculate(input, actions);
        boolean valid = draw < threshold;
        trace("[{}] @ [{}] {} ({} < {})", getName(), input.getAgentName(), valid, draw, threshold);
        return valid;
    }
}
