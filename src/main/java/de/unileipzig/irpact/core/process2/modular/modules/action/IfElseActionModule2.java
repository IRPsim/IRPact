package de.unileipzig.irpact.core.process2.modular.modules.action;

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
public class IfElseActionModule2<I>
        extends AbstractGenericMultiModuleBase2<I, Void, I>
        implements VoidModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IfElseActionModule2.class);

    protected BooleanModule2<I> test;
    protected VoidModule2<I> onTrue;
    protected VoidModule2<I> onFalse;

    public void setTest(BooleanModule2<I> test) {
        this.test = test;
    }

    public BooleanModule2<I> getTest() {
        return test;
    }

    public void setOnTrue(VoidModule2<I> onTrue) {
        this.onTrue = onTrue;
    }

    public VoidModule2<I> getOnTrue() {
        return onTrue;
    }

    public void setOnFalse(VoidModule2<I> onFalse) {
        this.onFalse = onFalse;
    }

    public VoidModule2<I> getOnFalse() {
        return onFalse;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
        traceModuleValidation();
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
    }

    @Override
    public int getSubmoduleCount() {
        return 3;
    }

    @Override
    public Module2<I, ?> getSubmodule(int index) {
        switch (index) {
            case 0:
                return test;
            case 1:
                return onTrue;
            case 2:
                return onFalse;
            default:
                throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    @Override
    public void run(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();

        if(test.test(input, actions)) {
            onTrue.run(input, actions);
        } else {
            onFalse.run(input, actions);
        }
    }
}
