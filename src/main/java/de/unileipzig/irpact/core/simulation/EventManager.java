package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.Event;

/**
 * @author Daniel Abitz
 */
public interface EventManager {

    void schedule(Event event);
}
