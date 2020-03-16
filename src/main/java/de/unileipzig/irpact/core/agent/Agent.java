package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.message.MessageContent;
import de.unileipzig.irpact.core.message.MessageReceiver;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface Agent extends SimulationEntity, MessageReceiver {

    @Override
    AgentIdentifier getIdentifier();

    @Override
    default boolean isHandling(Agent sender, MessageContent content) {
        return false;
    }

    @Override
    default void handleMessage(Agent sender, MessageContent content) {
    }
}
