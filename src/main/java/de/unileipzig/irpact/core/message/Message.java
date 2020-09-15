package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.dev.Experimental;
import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface Message {

    void process(Agent sender, Agent receiver);

    @Experimental
    default boolean isSerializable() {
        return false;
    }

    @Experimental
    default String serializeToString() {
        throw new UnsupportedOperationException();
    }
}
