package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.events.Event;
import de.unileipzig.irpact.core.events.ScheduledEvent;

/**
 * @author Daniel Abitz
 */
public interface EventManager {

    void schedule(Event event);

    void schedule(ScheduledEvent event);

    void scheduleAt(Event event, Timestamp schedulePoint);
}
