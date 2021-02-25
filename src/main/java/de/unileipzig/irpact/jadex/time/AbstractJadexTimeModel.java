package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractJadexTimeModel extends NameableBase implements JadexTimeModel {

    protected JadexSimulationEnvironment environment;
    protected JadexTimestamp startTime;
    protected JadexTimestamp endTime;

    public AbstractJadexTimeModel() {
    }

    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
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
        return environment.getLiveCycleControl().getClockService();
    }

    @Override
    public ISimulationService getSimulationService() {
        return environment.getLiveCycleControl().getSimulationService();
    }

    @Override
    public boolean isValid(Timestamp ts) {
        if(startTime == null || endTime == null || ts == null) {
            return false;
        }
        return ts.isAfterOrEquals(startTime) && ts.isBeforeOrEqual(endTime);
    }

    @Override
    public boolean endTimeReached() {
        Timestamp now = now();
        return now.isAfterOrEquals(endTime);
    }
}
