package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentIdentifier;

/**
 * @author Daniel Abitz
 */
public interface MessageSystem {

    void send(AgentIdentifier from, MessageContent content, AgentIdentifier to);

    void send(AgentIdentifier from, MessageContent content, AgentIdentifier... to);

    void send(Agent from, MessageContent content, Agent to);

    void send(Agent from, MessageContent content, Agent... to);
}
