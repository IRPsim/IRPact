package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.SpecialUtilityCsvValueLoggingModule2;
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
    protected int specialId = SpecialUtilityCsvValueLoggingModule2.UNSET_ID;

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

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
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

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setX(CalculationModule2<I> xModule) {
        setSubmodule1(xModule);
    }

    public CalculationModule2<I> getX() {
        return getNonnullSubmodule1();
    }

    public void setX0(CalculationModule2<I> xModule) {
        setSubmodule2(xModule);
    }

    public CalculationModule2<I> getX0() {
        return getNonnullSubmodule2();
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        double x = getX().calculate(input, actions);
        checkAndWarnNaN(input, x, getX(), "x");
        double x0 = getX0().calculate(input, actions);
        checkAndWarnNaN(input, x0, getX0(), "x0");
        double r = MathUtil.logistic(getL(), getK(), x, x0);
        checkAndWarnNaN(input, r, "result");
        setSpecialData(specialId, r, input);
        return r;
    }
}
