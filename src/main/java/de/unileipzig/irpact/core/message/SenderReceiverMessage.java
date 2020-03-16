package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface SenderReceiverMessage extends MessageContent {

    Agent getSender();

    Agent getReceiver();
}
