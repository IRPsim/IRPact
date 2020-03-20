package de.unileipzig.irpact.core.events;

import de.unileipzig.irpact.core.simulation.Timestamp;

/**
 * @author Daniel Abitz
 */
public interface ScheduledEvent extends Event {

    Timestamp getSchedulePoint();
}
