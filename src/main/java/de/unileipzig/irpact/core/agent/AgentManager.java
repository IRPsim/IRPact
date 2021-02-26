package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.util.Todo;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
//Listener support fuer neue Gruppen?
@Todo("ProcessPlan auch Agenten austauschen!!! -> wenn er dem Jadexagenten uebergeben wird")
public interface AgentManager extends Initialization {

    //=========================
    //general
    //=========================

    Collection<ConsumerAgentGroup> getConsumerAgentGroups();

    boolean hasConsumerAgentGroup(String name);

    ConsumerAgentGroup getConsumerAgentGroup(String name);

    default Stream<ConsumerAgent> streamConsumerAgents() {
        return getConsumerAgentGroups().stream()
                .flatMap(cag -> cag.getAgents().stream());
    }

    default Stream<ConsumerAgentGroup> streamConsumerAgentGroups() {
        return getConsumerAgentGroups().stream();
    }

    int getTotalNumberOfConsumerAgents();

    ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping();

    void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping);
}
