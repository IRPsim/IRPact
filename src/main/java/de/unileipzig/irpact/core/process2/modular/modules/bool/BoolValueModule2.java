package de.unileipzig.irpact.core.process2.modular.modules.bool;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.*;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class BoolValueModule2<I>
        extends AbstractBooleanModule2<I>
        implements HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BoolValueModule2.class);

    protected boolean value;

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
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
        traceModuleInitalization();
    }

    @Override
    public boolean test(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();
        return getValue();
    }
}
