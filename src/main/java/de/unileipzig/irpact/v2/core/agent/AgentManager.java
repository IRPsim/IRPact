package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinityMapping;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface AgentManager {

    Set<ConsumerAgentGroup> getConsumerAgentGroups();

    ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping();
}
