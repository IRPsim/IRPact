package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.misc.Initialization;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
//Listener support fuer neue Gruppen?
public interface AgentManager extends Initialization {

    //=========================
    //general
    //=========================

    Collection<ConsumerAgentGroup> getConsumerAgentGroups();

    ConsumerAgentGroup getConsumerAgentGroup(String name);

    boolean add(ConsumerAgentGroup group);

    default Stream<ConsumerAgent> streamConsumerAgents() {
        return getConsumerAgentGroups().stream()
                .flatMap(cag -> cag.getAgents().stream());
    }

    default Stream<ConsumerAgentGroup> streamConsumerAgentGroups() {
        return getConsumerAgentGroups().stream();
    }

    int getTotalNumberOfConsumerAgents();

    void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int count);

    int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group);

    ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping();

    void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping);

    void replacePlaceholder(ConsumerAgent realAgent) throws IllegalStateException;
}
