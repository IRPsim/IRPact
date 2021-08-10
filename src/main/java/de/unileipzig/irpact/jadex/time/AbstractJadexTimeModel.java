package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.jadex.simulation.JadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractJadexTimeModel extends NameableBase implements JadexTimeModel {

    protected JadexSimulationEnvironment environment;
    protected JadexTimestamp startTime;
    protected JadexTimestamp endTime;
    protected long currentYearForValidation;

    public AbstractJadexTimeModel() {
    }

    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }

    protected JadexLifeCycleControl lifeCycleControl() {
        return environment.getLifeCycleControl();
    }

    @Override
    public int getCurrentYear() {
        return now().getYear();
    }

    @Override
    public JadexTimestamp startTime() {
        return startTime;
    }

    @Override
    public JadexTimestamp endTime() {
        return endTime;
    }

    @Override
    public double getTimeProgress() {
        long total = ChronoUnit.MILLIS.between(startTime().getTime(), endTime().getTime());
        long passed = ChronoUnit.MILLIS.between(startTime().getTime(), now().getTime());
        return (double) passed / (double) total;
    }

    @Override
    public IClockService getClockService() {
        return environment.getLifeCycleControl().getClockService();
    }

    @Override
    public ISimulationService getSimulationService() {
        return environment.getLifeCycleControl().getSimulationService();
    }

    @Override
    public boolean isValid(Timestamp ts) {
        if(startTime == null || endTime == null || ts == null) {
            return false;
        }
        return ts.isBetween(startTime, endTime);
    }

    @Override
    public boolean endTimeReached() {
        Timestamp time = endTime;
        if(time == null) {
            return true;
        }
        Timestamp now = now();
        return time.isBefore(now);
    }
}
