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
    protected boolean usePostactions = true;

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
        traceModuleCall(input);

        if(usePostactions) {
            if(actions == null) {
                run0(input);
            } else {
                PostAction2 action = runAsAction(input);
                trace("[{}] new post action '{}'", action.getName());
                actions.add(action);
            }
        } else {
            run0(input, actions);
        }
    }

    protected PostAction2 runAsAction(I input) {
        return new PostAction2() {
            @Override
            public String getName() {
                return "PostAction@" + IfElseActionModule2.this.getName() + "@" + printInputInfo(input);
            }

            @Override
            public void execute2() throws Throwable {
                run0(input);
            }
        };
    }

    protected void run0(I input) throws Throwable {
        run0(input, null);
    }

    protected void run0(I input, List<PostAction2> actions) throws Throwable {
        if(test.test(input, actions)) {
            onTrue.run(input, actions);
        } else {
            onFalse.run(input, actions);
        }
    }
}
