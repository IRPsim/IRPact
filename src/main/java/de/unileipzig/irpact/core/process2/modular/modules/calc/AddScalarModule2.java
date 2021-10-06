package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.modules.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AddScalarModule2<I>
        extends AbstractUniformMultiModule1_2<I, Number, I, Number, CalculationModule2<I>>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AddScalarModule2.class);

    protected double scalar;

    public void setScalar(double scalar) {
        this.scalar = scalar;
    }

    public double getScalar() {
        return scalar;
    }

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo();
        return getScalar() + getNonnullSubmodule().calculate(input, actions);
    }
}
