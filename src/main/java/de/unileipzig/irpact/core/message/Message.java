package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface Message {

    void process(Agent sender, Agent receiver);

    boolean isSerializable();

    String serializeToString();
}
