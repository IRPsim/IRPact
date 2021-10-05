package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.modules.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule2_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class LogisticModule2<I>
        extends AbstractUniformMultiModule2_2<I, Number, I, Number, CalculationModule2<I>>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LogisticModule2.class);

    protected double L = 1.0;
    protected double k = 1.0;

    public void setL(double l) {
        L = l;
    }

    public double getL() {
        return L;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getK() {
        return k;
    }

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
        double x = getNonnullSubmodule1().calculate(input, actions);
        double x0 = getNonnullSubmodule2().calculate(input, actions);
        return MathUtil.logistic(L, k, x, x0);
    }
}
