package de.unileipzig.irpact.jadex.agents;

import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractJadexAgentBDI extends AbstractAgentBase {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    //AENDERN
    protected long delay = 1L;

    public AbstractJadexAgentBDI() {
    }

    @Override
    protected final void scheduleFirstAction() {
        getTimeModel().scheduleImmediately(
                execFeature,
                agent,
                ia -> {
                    try {
                        firstAction();
                        return IFuture.DONE;
                    } catch (Throwable t) {
                        log().error("[{}] first action failed", getName());
                        getEnvironment().getLifeCycleControl().handleFatalError(t);
                        return IFuture.DONE;
                    }
                }
        );
    }

    @Override
    protected final void scheduleLoop() {
        scheduleNextLoopStep();
    }

    protected void scheduleNextLoopStep() {
        IFuture<Void> nextTask = getTimeModel().wait(
                execFeature,
                delay,
                agent,
                LOOP_STEP
        );

        if(JadexUtil.endTimeReached(nextTask)) {
            log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] loop finished ({})", getName(), now().printComplex());
        }
    }

    protected final IComponentStep<Void> LOOP_STEP = this::loopStep;
    protected IFuture<Void> loopStep(IInternalAccess access) {
        if(isTerminated()) {
            log().trace("[{}] cancel loop - simulation terminated", getName());
            return IFuture.DONE;
        }

        try {
            pulse();
            onLoopAction();
        } catch (Throwable t) {
            log().error("[{}] loop failed ({})", getName(), now());
            getEnvironment().getLifeCycleControl().handleFatalError(t);
            return IFuture.DONE;
        }

        scheduleNextLoopStep();
        return IFuture.DONE;
    }

    protected boolean isTerminated() {
        return getLifeCycleControl().getTerminationState() != LifeCycleControl.TerminationState.RUNNING;
    }
}
