package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAPlanEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.process2.modular.modules.core.MultiModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Deque;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class RunUntilFailureModule2
        extends AbstractCAPlanEvaluationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RunUntilFailureModule2.class);

    protected Module2<ConsumerAgentData2, ?> submodule;
    protected SimulationEnvironment environment;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        if(submodule == null) {
            throw new NullPointerException("missing submodule");
        }
        submodule.validate();
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        this.sharedData = sharedData;
        submodule.setSharedData(sharedData);
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
        this.environment = environment;
        submodule.initialize(environment);
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
        submodule.initializeNewInput(input);
    }

    @Override
    public Module2<?, ?> containsLoop(Deque<Module2<?, ?>> currentPath, Set<Module2<?, ?>> allModules) {
        return MultiModule2.containsLoop(currentPath, allModules, this, submodule);
    }

    public void setSubmodule(Module2<ConsumerAgentData2, ?> submodule) {
        this.submodule = submodule;
    }

    public Module2<ConsumerAgentData2, ?> getSubmodule() {
        return submodule;
    }

    public Module2<ConsumerAgentData2, ?> getNonnullSubmodule() {
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }

    @Override
    public ProcessPlanResult2 apply(ConsumerAgentData2 input, List<PostAction2> actions) {
        traceModuleCall(input);

        Module2<ConsumerAgentData2, ?> submodul = getNonnullSubmodule();
        try {
            Object result = submodul.apply(input, actions);
            trace("submodule '{}' result: {}", submodul.getName(), result);
            return ProcessPlanResult2.CONTINUE;
        } catch (Throwable e) {
            error("submodule '" + submodul.getName() + "' failed", e);
            if(environment != null) {
                environment.getLifeCycleControl().handleFatalError(e);
            }
            return ProcessPlanResult2.FINISHED;
        }
    }
}
