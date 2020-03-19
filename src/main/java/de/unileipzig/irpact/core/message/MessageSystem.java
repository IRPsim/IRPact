package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface MessageSystem {

    void send(Agent from, Message msg, Agent to);

    void send(Agent from, Message msg, Agent... to);

    void send(Agent from, Message msg, Iterable<? extends Agent> to);
}
