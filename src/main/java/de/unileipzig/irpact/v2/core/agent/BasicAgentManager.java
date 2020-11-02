package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinityMapping;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicAgentManager implements AgentManager {

    protected Set<ConsumerAgentGroup> consumerAgentGroups;
    protected ConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
    protected Map<ConsumerAgentGroup, Integer> agentCount;

    public BasicAgentManager() {
        this(new HashSet<>(), new HashMap<>());
    }

    public BasicAgentManager(Set<ConsumerAgentGroup> consumerAgentGroups, Map<ConsumerAgentGroup, Integer> agentCount) {
        this.consumerAgentGroups = consumerAgentGroups;
        this.agentCount = agentCount;
    }

    @Override
    public boolean add(ConsumerAgentGroup group) {
        return consumerAgentGroups.add(group);
    }

    public void setConsumerAgentGroups(Set<ConsumerAgentGroup> consumerAgentGroups) {
        this.consumerAgentGroups = consumerAgentGroups;
    }

    @Override
    public Set<ConsumerAgentGroup> getConsumerAgentGroups() {
        return consumerAgentGroups;
    }

    @Override
    public ConsumerAgentGroup getConsumerAgentGroup(String name) {
        for(ConsumerAgentGroup cag: consumerAgentGroups) {
            if(Objects.equals(cag.getName(), name)) {
                return cag;
            }
        }
        return null;
    }

    public void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping) {
        this.affinityMapping = affinityMapping;
    }

    @Override
    public ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping() {
        return affinityMapping;
    }

    @Override
    public void setNumberOfAgents(ConsumerAgentGroup group, int count) {
        agentCount.put(group, count);
    }

    @Override
    public int getNumberOfAgents(ConsumerAgentGroup group) {
        return agentCount.getOrDefault(group, 0);
    }

    @Override
    public int sumNumberOfConsumerAgents() {
        return agentCount.values()
                .stream()
                .mapToInt(v -> v)
                .sum();
    }
}
