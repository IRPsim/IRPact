package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.Event;

/**
 * @author Daniel Abitz
 */
public class BasicJadexEventManager implements JadexEventManager {

    @Override
    public void schedule(Event event) {
        event.process();
    }
}
