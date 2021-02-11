package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicInitializationData implements InitializationData {

    protected Map<ConsumerAgentGroup, Integer> agentCount;

    public BasicInitializationData() {
        this(new HashMap<>());
    }

    public BasicInitializationData(Map<ConsumerAgentGroup, Integer> agentCount) {
        this.agentCount = agentCount;
    }

    @Override
    public void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int count) {
        agentCount.put(group, count);
    }

    @Override
    public int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group) {
        return agentCount.get(group);
    }
}
