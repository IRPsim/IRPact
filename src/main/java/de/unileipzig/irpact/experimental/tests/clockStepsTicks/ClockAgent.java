package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import de.unileipzig.irpact.experimental.ExperimentalUtil;
import de.unileipzig.irpact.experimental.todev.ScheduledTasks;
import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.time.LocalTime;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Agent
@Service
@ProvidedServices({
        @ProvidedService(type = ClockAgentService.class, scope = ServiceScope.NETWORK)
})
public class ClockAgent implements ClockAgentService {

    public static ClockAgent AGENT;

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IExecutionFeature execFeature;

    protected boolean running;
    protected IClockService clock;
    protected ScheduledTasks<Double, ScheduledTask<Void>> scheduledTasks = new ScheduledTasks<>();

    public ClockAgent() {
        AGENT = this;
    }

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + clock.getTime() + "]"
                + " [" + clock.getTick() + "]"
                + " [CLOCK]"
                + " " + msg);
    }

    //=========================
    //Clock-Stuff
    //=========================

    private void check() {
        running = current() < 100; //nach 100 ticks terminate
    }

    private double current() {
        return clock.getTick();
    }

    private long tickLength() {
        return clock.getDelta();
    }

    private boolean isRunning() {
        return running;
    }

    private final IComponentStep<Void> runTickCycleStep = this::runTickCycle;
    private IFuture<Void> runTickCycle(IExternalAccess access) {
        check();
        List<ScheduledTask<Void>> pendingTasks = scheduledTasks.removeAllPendingTasks(current());
        if(pendingTasks.size() > 0) {
            log("pending: " + pendingTasks.size());
        }
        for(ScheduledTask<Void> task: pendingTasks) {
            //IFuture<Void> taskResult = task.getExec().waitForTick(task.getTask());
            //task.setRealFuture(taskResult);
        }
        if(isRunning()) {
            execFeature.waitForTick(runTickCycleStep);
        } else {
            log("TERMINATED");
        }
        return IFuture.DONE;
    }

    //=========================
    //ClockAgentService
    //=========================

    private IFuture<Void> registerAt(double tick, Task task) {
        ScheduledTask<Void> scheduledTask = new ScheduledTask<>(task);
        scheduledTasks.register(tick, scheduledTask);
        return scheduledTask;
    }

    @Override
    public IFuture<Void> scheduleAt(double tick, Task task) {
        if(current() < tick) {
            return registerAt(tick, task);
        }
        return IFuture.DONE;
    }


    @Override
    public IFuture<Void> waitForTick(Task task) {
        return registerAt(current(), task);
    }

    @Override
    public IFuture<Void> waitForTicks(double ticks, Task task) {
        if(ticks < 1) {
            return waitForTick(task);
        } else {
            return scheduleAt(current() + ticks, task);
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        clock = ExperimentalUtil.getClockService(agent);
        log("onInit");
        execFeature.waitForTick(runTickCycleStep);
    }

    @OnStart
    protected void onStart() {
        log("onStart");
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }

    @Override
    public String toString() {
        return "CLOCKAGENT";
    }
}
