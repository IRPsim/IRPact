package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCARAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.process2.modular.modules.core.MultiModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.VoidModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Deque;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class MainBranchingModule2
        extends AbstractCARAEvaluationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MainBranchingModule2.class);

    protected RAEvaluationModule2<ConsumerAgentData2> initModule;
    protected RAEvaluationModule2<ConsumerAgentData2> awarenessModule;
    protected RAEvaluationModule2<ConsumerAgentData2> feasibilityModule;
    protected RAEvaluationModule2<ConsumerAgentData2> decisionModule;
    protected VoidModule2<ConsumerAgentData2> adopterModule;
    protected VoidModule2<ConsumerAgentData2> impededModule;

    public void setInitModule(RAEvaluationModule2<ConsumerAgentData2> initModule) {
        this.initModule = initModule;
    }

    public void setAwarenessModule(RAEvaluationModule2<ConsumerAgentData2> awarenessModule) {
        this.awarenessModule = awarenessModule;
    }

    public void setFeasibilityModule(RAEvaluationModule2<ConsumerAgentData2> feasibilityModule) {
        this.feasibilityModule = feasibilityModule;
    }

    public void setDecisionModule(RAEvaluationModule2<ConsumerAgentData2> decisionModule) {
        this.decisionModule = decisionModule;
    }

    public void setAdopterModule(VoidModule2<ConsumerAgentData2> adopterModule) {
        this.adopterModule = adopterModule;
    }

    public void setImpededModule(VoidModule2<ConsumerAgentData2> impededModule) {
        this.impededModule = impededModule;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        super.setSharedData(sharedData);
        MultiModule2.setSharedDataAll(sharedData, initModule, awarenessModule, feasibilityModule, decisionModule, adopterModule, impededModule);
    }

    @Override
    public void validate() throws Throwable {
        if(initModule == null) throw new NullPointerException("initModule");
        if(awarenessModule == null) throw new NullPointerException("awarenessModule");
        if(feasibilityModule == null) throw new NullPointerException("feasibilityModule");
        if(decisionModule == null) throw new NullPointerException("decisionModule");
        if(adopterModule == null) throw new NullPointerException("adopterModule");
        if(impededModule == null) throw new NullPointerException("impededModule");

        MultiModule2.validateAll(initModule, awarenessModule, feasibilityModule, decisionModule, adopterModule, impededModule);
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        MultiModule2.initalizeAll(environment, initModule, awarenessModule, feasibilityModule, decisionModule, adopterModule, impededModule);
    }

    @Override
    public Module2<?, ?> containsLoop(Deque<Module2<?, ?>> currentPath, Set<Module2<?, ?>> allModules) {
        return MultiModule2.containsLoop(
                currentPath,
                allModules,
                this,
                initModule,
                awarenessModule,
                feasibilityModule,
                decisionModule,
                adopterModule,
                impededModule
        );
    }

    protected RAStage2 initalize(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        RAStage2 nextStage = initModule.apply(input, actions);
        return execute(nextStage, input, actions);
    }

    protected RAStage2 execute(RAStage2 stage, ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        switch (stage) {
            case AWARENESS:
                return awarenessModule.apply(input, actions);

            case FEASIBILITY:
                return feasibilityModule.apply(input, actions);

            case DECISION_MAKING:
                doSelfActionAndAllowAttention(input);
                return decisionModule.apply(input, actions);

            case ADOPTED:
                allowAttention(input);
                adopterModule.apply(input, actions);
                return RAStage2.ADOPTED;

            case IMPEDED:
                allowAttention(input);
                impededModule.apply(input, actions);
                return RAStage2.ADOPTED;

            default:
                throw new IllegalStateException("unsupported phase: " + stage);
        }
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        RAStage2 stage = input.getStage();
        if(stage == RAStage2.PRE_INITIALIZATION) {
            return initalize(input, actions);
        } else {
            return execute(stage, input, actions);
        }
    }
}
