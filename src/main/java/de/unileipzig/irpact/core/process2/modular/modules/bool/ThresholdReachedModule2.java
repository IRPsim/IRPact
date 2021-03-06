package de.unileipzig.irpact.core.process2.modular.modules.bool;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule2_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.BooleanModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ThresholdReachedModule2<I>
        extends AbstractUniformMultiModule2_2<I, Boolean, I, Number, CalculationModule2<I>>
        implements BooleanModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ThresholdReachedModule2.class);

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
    protected I castInput(I input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(I input) throws Throwable {
    }

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
    }

    public void setDrawModule(CalculationModule2<I> module) {
        setSubmodule1(module);
    }

    public CalculationModule2<I> getDrawModule() {
        return getNonnullSubmodule1();
    }

    public void setThresholdModule(CalculationModule2<I> module) {
        setSubmodule2(module);
    }

    public CalculationModule2<I> getThresholdModule() {
        return getNonnullSubmodule2();
    }

    @Override
    public boolean test(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        double draw = getDrawModule().calculate(input, actions);
        double threshold = getThresholdModule().calculate(input, actions);
        boolean valid = threshold <= draw;
        trace("[{}]@[{}] threshold reached: {} (threshold={} <= draw={})", getName(), printInputInfo(input), valid, threshold, draw);
        return valid;
    }
}
