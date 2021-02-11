package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface InitializationData {

    void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int count);

    int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group);
}
