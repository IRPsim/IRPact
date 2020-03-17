package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 * @implNote Dem Nachrichtensystem von Jadex angelehnt.
 */
public interface MessageReceiver {

    boolean isHandling(Agent sender, MessageContent content);

    void handleMessage(Agent sender, MessageContent content);
}
