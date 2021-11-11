package de.unileipzig.irpact.core.process2.modular.modules.bool;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.modules.core.*;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class IfDoActionModule2<I>
        extends AbstractBooleanModule2<I>
        implements MultiModule2<I, Boolean>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IfDoActionModule2.class);

    protected BooleanModule2<I> ifModule;
    protected VoidModule2<I> taskModule;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        ifModule.validate();
        taskModule.validate();
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        this.sharedData = sharedData;
        ifModule.setSharedData(sharedData);
        taskModule.setSharedData(sharedData);
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
        ifModule.initialize(environment);
        taskModule.initialize(environment);
    }

    @Override
    public void initializeNewInput(I input) throws Throwable {
        traceNewInput(input);
    }

    public void setIfModule(BooleanModule2<I> ifModule) {
        this.ifModule = ifModule;
    }

    public BooleanModule2<I> getIfModule() {
        return ifModule;
    }

    public void setTaskModule(VoidModule2<I> taskModule) {
        this.taskModule = taskModule;
    }

    public VoidModule2<I> getTaskModule() {
        return taskModule;
    }

    @Override
    public int getSubmoduleCount() {
        return 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> Module2<A, B> getSubmodule(int index) {
        switch(index) {
            case 0:
                return (Module2<A, B>) ifModule;
            case 1:
                return (Module2<A, B>) taskModule;
            default:
                throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    @Override
    public boolean test(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        if(ifModule.test(input, actions)) {
            taskModule.run(input, actions);
            return true;
        } else {
            return false;
        }
    }
}
