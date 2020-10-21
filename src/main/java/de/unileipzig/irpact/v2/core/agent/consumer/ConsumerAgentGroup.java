package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.core.agent.AgentGroup;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Set<ConsumerAgentGroupAttribute> getAttributes();
}
