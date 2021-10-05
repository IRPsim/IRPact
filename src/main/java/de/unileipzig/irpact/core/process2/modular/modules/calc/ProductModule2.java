package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.modules.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModuleN_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ProductModule2<I>
        extends AbstractUniformMultiModuleN_2<I, Number, I, Number, CalculationModule2<I>>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductModule2.class);

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initalizeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo();
        double sum = 1.0;
        for(int i = 0; i < getSubmoduleCount(); i++) {
            sum *= getNonnullSubmodule(i).calculate(input, actions);
        }
        return sum;
    }
}
