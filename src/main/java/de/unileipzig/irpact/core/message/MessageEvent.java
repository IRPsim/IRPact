package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.Event;

/**
 * @author Daniel Abitz
 */
public interface MessageEvent extends Event {

    Message getMessage();
}
