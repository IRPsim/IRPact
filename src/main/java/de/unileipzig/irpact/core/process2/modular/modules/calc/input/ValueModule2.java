package de.unileipzig.irpact.core.process2.modular.modules.calc.input;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ValueModule2<I>
        extends AbstractModule2<I, Number>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ValueModule2.class);

    protected double value;

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

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
        if(alreadyInitalized()) {
            return;
        }

        traceModuleInitalization();
        trace("[{}] value={}", getName(), getValue());
        setInitalized();
    }

    @Override
    public void initializeNewInput(I input) throws Throwable {
        traceNewInput(input);
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        traceModuleSetup();
        setSetupCalled();
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        return getValue();
    }
}
