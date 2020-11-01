package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.commons.time.Timestamp;
import jadex.bridge.IComponentStep;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractJadexTimeModel implements JadexTimeModel {

    protected IClockService clock;
    protected ISimulationService simulation;
    protected JadexTimestamp start;
    protected JadexTimestamp end;

    public AbstractJadexTimeModel() {
    }

    public void setStart(JadexTimestamp start) {
        this.start = start;
    }

    public JadexTimestamp getStart() {
        return start;
    }

    public void setEnd(JadexTimestamp end) {
        this.end = end;
    }

    public JadexTimestamp getEnd() {
        return end;
    }

    @Override
    public IClockService getClockService() {
        return clock;
    }

    public void setClock(IClockService clock) {
        this.clock = clock;
    }

    public void setSimulation(ISimulationService simulation) {
        this.simulation = simulation;
    }

    @Override
    public ISimulationService getSimulationService() {
        return simulation;
    }

    @Override
    public IFuture<Void> waitUntilEnd(IExecutionFeature exec, IComponentStep<Void> task) {
        return waitUntil0(exec, getEnd(), task);
    }

    @Override
    public boolean isValid(long delayInMs) {
        delayInMs = Math.max(delayInMs, 0L);
        JadexTimestamp ts = plusMillis(delayInMs);
        return isValid(ts);
    }

    @Override
    public boolean isValid(Timestamp ts) {
        if(start == null || end == null || ts == null) {
            return false;
        }
        return ts.isAfterOrEquals(start) && ts.isBeforeOrEqual(end);
    }
}
