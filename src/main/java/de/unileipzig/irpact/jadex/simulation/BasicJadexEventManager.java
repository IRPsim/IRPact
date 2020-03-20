package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.core.events.Event;
import de.unileipzig.irpact.core.events.ScheduledEvent;
import de.unileipzig.irpact.core.simulation.TimeModule;
import de.unileipzig.irpact.core.simulation.Timestamp;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.bridge.component.IExternalExecutionFeature;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public class BasicJadexEventManager implements JadexEventManager {

    protected JadexSimulationEnvironment environment;

    public BasicJadexEventManager(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void schedule(Event event) {
        event.process();
    }

    @Override
    public void schedule(ScheduledEvent event) {
        scheduleAt(event, event.getSchedulePoint());
    }

    @Experimental
    @Override
    public void scheduleAt(Event event, Timestamp schedulePoint) {
        IExternalExecutionFeature exec = environment.getPlatformExec();
        TimeModule timeModule = environment.getTimeModule();
        switch (schedulePoint.getMode()) {
            case SYSTEM:
                long systemDelta = schedulePoint.getSystemTime() - timeModule.getSystemTime();
                if(systemDelta >= 0) {
                    exec.waitForDelay(
                            systemDelta,
                            ia -> {
                                event.process();
                                return IFuture.DONE;
                            },
                            true
                    );
                } else {
                    //error/ignore?
                }
                break;
            case SIMULATION:
                long simulationDelta = schedulePoint.getSimulationTime() - timeModule.getSimulationTime();
                if(simulationDelta >= 0) {
                    exec.waitForDelay(
                            simulationDelta,
                            ia -> {
                                event.process();
                                return IFuture.DONE;
                            },
                            false
                    );
                } else {
                    //error/ignore?
                }
                break;
            case TICK:
                double tickDelta = schedulePoint.getTick() - timeModule.getTick();
                if(tickDelta > 0.0) {
                    JadexUtil.waitForTick(
                            exec,
                            (int) tickDelta,
                            ia -> {
                                event.process();
                                return IFuture.DONE;
                            }
                    );
                } else {
                    //error/ignore?
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }
}
