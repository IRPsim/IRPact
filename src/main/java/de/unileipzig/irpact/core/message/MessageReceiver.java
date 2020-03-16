package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @implNote Dem Nachrichtensystem von Jadex angelehnt.
 * @author Daniel Abitz
 */
public interface MessageReceiver {

    boolean isHandling(Agent sender, MessageContent content);

    void handleMessage(Agent sender, MessageContent content);
}
