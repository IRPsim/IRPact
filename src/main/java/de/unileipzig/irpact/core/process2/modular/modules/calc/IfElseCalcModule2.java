package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.*;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class IfElseCalcModule2<I>
        extends AbstractModule2<I, Number>
        implements CalculationModule2<I>, MultiModule2<I, Number>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IfElseCalcModule2.class);

    protected BooleanModule2<I> checkModule;
    protected CalculationModule2<I> trueModule;
    protected CalculationModule2<I> falseModule;

    protected boolean forceEvaluation = false;
    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }
    public boolean isForceEvaluation() {
        return forceEvaluation;
    }
    public boolean isNotForceEvaluation() {
        return !forceEvaluation;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        checkModule.validate();
        trueModule.validate();
        falseModule.validate();
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
        if(checkModule == null) {
            throw new NullPointerException("checkModule");
        }
        if(trueModule == null) {
            throw new NullPointerException("trueModule");
        }
        if(falseModule == null) {
            throw new NullPointerException("trueModule");
        }
    }

    protected double apply0(
            I input,
            List<PostAction2> actions) throws Throwable {
        traceModuleCall();

        boolean failed = !checkModule.test(input, actions);
        if(failed && isNotForceEvaluation()) {
            return falseModule.calculate(input, actions);
        } else {
            double trueResult = trueModule.calculate(input, actions);
            return failed
                    ? falseModule.calculate(input, actions)
                    : trueResult;
        }
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        return apply0(input, actions);
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        this.sharedData = sharedData;
        checkModule.setSharedData(sharedData);
        trueModule.setSharedData(sharedData);
        falseModule.setSharedData(sharedData);
    }

    @Override
    public int getSubmoduleCount() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> Module2<A, B> getSubmodule(int index) {
        Module2<?, ?> m;
        switch (index) {
            case 0:
                m = checkModule;
                break;
            case 1:
                m = trueModule;
                break;
            case 2:
                m = falseModule;
                break;
            default:
                throw new IndexOutOfBoundsException("index: " + index);
        }
        return (Module2<A, B>) m;
    }
}
