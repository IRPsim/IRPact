package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface Message {

    Agent getSender();

    Agent getReceiver();

    MessageContent getContent();
}
