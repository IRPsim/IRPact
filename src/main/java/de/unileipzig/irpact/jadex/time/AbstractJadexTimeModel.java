package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractJadexTimeModel implements JadexTimeModel {

    protected IClockService clock;
    protected ISimulationService simulation;
    protected JadexTimestamp startTime;
    protected JadexTimestamp endTime;

    public AbstractJadexTimeModel() {
    }

    public void setStartTime(JadexTimestamp startTime) {
        this.startTime = startTime;
    }

    @Override
    public int getYear() {
        JadexTimestamp now = now();
        ZonedDateTime zdt = now.getTime();
        return zdt.getYear();
    }

    @Override
    public JadexTimestamp startTime() {
        return startTime;
    }

    public void setEndTime(JadexTimestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public JadexTimestamp endTime() {
        return endTime;
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
    public boolean isValid(Timestamp ts) {
        if(startTime == null || endTime == null || ts == null) {
            return false;
        }
        return ts.isAfterOrEquals(startTime) && ts.isBeforeOrEqual(endTime);
    }
}
