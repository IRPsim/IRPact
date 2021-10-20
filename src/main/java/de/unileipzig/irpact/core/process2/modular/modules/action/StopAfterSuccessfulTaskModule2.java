package de.unileipzig.irpact.core.process2.modular.modules.action;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModuleN_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.BooleanModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.VoidModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class StopAfterSuccessfulTaskModule2<I>
        extends AbstractUniformMultiModuleN_2<I, Void, I, Boolean, BooleanModule2<I>>
        implements VoidModule2<I>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(StopAfterSuccessfulTaskModule2.class);

    protected boolean sort = true;
    protected boolean useActions = true;

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public boolean isSort() {
        return sort;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
        if(sort) {
            trace("[{}] sort submodules", getName());
            trace("[{}] unsorted order: {}", getName(), listSubmoduleNames());
            sortPriority();
            trace("[{}] sorted order: {}", getName(), listSubmoduleNames());
        } else {
            trace("[{}] use default order: {}", getName(), listSubmoduleNames());
        }
    }

    @Override
    public void run(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();

        if(useActions && actions != null) {
            actions.add(createPostAction(input, actions));
        } else {
            run0(input, actions);
        }
    }

    protected PostAction2 createPostAction(I input, List<PostAction2> actions) {
        return new PostAction2() {

            @Override
            public String getName() {
                return "PostAction_" + StopAfterSuccessfulTaskModule2.this.getName() + "@" + input.toString();
            }

            @Override
            public void execute2() throws Throwable {
                trace("[{}] execute", getName());
                run0(input, actions);
            }
        };
    }

    protected void run0(I input, List<PostAction2> actions) throws Throwable {
        for(BooleanModule2<I> submodule: MODULES) {
            boolean success = submodule.test(input, actions);
            trace("[{}] @ [{}] submodule '{}' result: {}", getName(), input, submodule.getName(), success);
            if(success) {
                break;
            }
        }
    }
}
