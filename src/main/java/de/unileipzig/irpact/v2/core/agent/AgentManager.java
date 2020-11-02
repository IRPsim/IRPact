package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinityMapping;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface AgentManager {

    boolean add(ConsumerAgentGroup group);

    Set<ConsumerAgentGroup> getConsumerAgentGroups();

    ConsumerAgentGroup getConsumerAgentGroup(String name);

    default Stream<ConsumerAgent> streamConsumerAgents() {
        return getConsumerAgentGroups().stream()
                .flatMap(cag -> cag.getAgents().stream());
    }

    ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping();

    void setNumberOfAgents(ConsumerAgentGroup group, int count);

    int getNumberOfAgents(ConsumerAgentGroup group);
}
