package de.unileipzig.irpact.core.events;

import de.unileipzig.irpact.core.simulation.Timestamp;

/**
 * @author Daniel Abitz
 */
public abstract class ScheduledEventBase implements ScheduledEvent {

    protected Timestamp schedulePoint;

    public ScheduledEventBase(Timestamp schedulePoint) {
        this.schedulePoint = schedulePoint;
    }

    @Override
    public Timestamp getSchedulePoint() {
        return schedulePoint;
    }
}
