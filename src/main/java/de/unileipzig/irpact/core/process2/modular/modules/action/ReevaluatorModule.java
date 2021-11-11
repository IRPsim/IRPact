package de.unileipzig.irpact.core.process2.modular.modules.action;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.modules.core.*;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ReevaluatorModule<I>
        extends AbstractVoidModule2<I>
        implements HelperAPI2, Reevaluator<I>, MultiModule2<I, Void> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ReevaluatorModule.class);

    protected final List<Module2<I, ?>> MODULES;

    public ReevaluatorModule() {
        this(new ArrayList<>());
    }

    public ReevaluatorModule(List<Module2<I, ?>> modules) {
        this.MODULES = modules;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getSubmoduleCount() {
        return MODULES.size();
    }

    public boolean addSubmodule(Module2<I, ?> module) {
        return MODULES.add(module);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> Module2<A, B> getSubmodule(int index) {
        return (Module2<A, B>) MODULES.get(index);
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        super.setSharedData(sharedData);
        for(Module2<I, ?> submodule: MODULES) {
            submodule.setSharedData(sharedData);
        }
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        for(Module2<I, ?> submodule: MODULES) {
            submodule.validate();
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        init(environment);
    }

    @Override
    public void initializeNewInput(I input) throws Throwable {
        traceNewInput(input);
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        init(environment);
    }

    protected void init(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }
        setInitalized();
        traceModuleInitalization();
        for(Module2<I, ?> submodule: MODULES) {
            submodule.initialize(environment);
        }
    }

    @Override
    public void reevaluate(I input, List<PostAction2> actions) throws Throwable {
        execute(input, actions);
    }

    @Override
    public void run(I input, List<PostAction2> actions) throws Throwable {
        execute(input, actions);
    }

    protected void execute(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        startReevaluatorCall();
        for(Module2<I, ?> submodule: MODULES) {
            trace("execute submodule '{}'", submodule.getName());
            Object result = submodule.apply(input, actions);
            trace("submodule '{}' result: {}", submodule.getName(), result);
        }
        finishReevaluatorCall();
    }
}
