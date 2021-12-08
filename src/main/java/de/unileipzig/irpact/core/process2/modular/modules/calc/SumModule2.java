package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModuleN_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SumModule2<I>
        extends AbstractUniformMultiModuleN_2<I, Number, I, Number, CalculationModule2<I>>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SumModule2.class);

    protected boolean checkNaN = true;

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

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        if(checkNaN) {
            return calculateWithNaNCheck(input, actions);
        } else {
            return calculateWithoutNaNCheck(input, actions);
        }
    }

    protected double calculateWithoutNaNCheck(I input, List<PostAction2> actions) throws Throwable {
        double sum = 0.0;
        for(int i = 0; i < getSubmoduleCount(); i++) {
            sum += getNonnullSubmodule(i).calculate(input, actions);
        }
        return sum;
    }

    protected double calculateWithNaNCheck(I input, List<PostAction2> actions) throws Throwable {
        boolean isNaN = false;
        double[] subvalues = new double[getSubmoduleCount()];
        for(int i = 0; i < getSubmoduleCount(); i++) {
            subvalues[i] = getNonnullSubmodule(i).calculate(input, actions);
            isNaN |= Double.isNaN(subvalues[i]);
        }

        if(isNaN) {
            logNaNWarning(input, subvalues);
        }

        double sum = 0;
        for(double v : subvalues) {
            sum += v;
        }
        return sum;
    }

    protected void logNaNWarning(I input, double[] subvalues) {
        int nanCount = 0;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < subvalues.length; i++) {
            if(Double.isNaN(subvalues[i])) {
                if(nanCount > 0) {
                    sb.append(",");
                }
                CalculationModule2<I> module = getNonnullSubmodule(i);
                sb.append("index=")
                        .append(i)
                        .append(",submodule={")
                        .append(module.getName())
                        .append(",")
                        .append(module.getClass().getName())
                        .append("}");
                nanCount++;
            }
        }
        warn(
                "[{}]@[{}] ({}) '{}' NaN(s) detected: [{}]",
                getName(), printInputInfo(input), printThisClass(),
                nanCount, sb
        );
    }
}
