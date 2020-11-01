package de.unileipzig.irpact.v2.core.agent.consumer;


import de.unileipzig.irpact.v2.commons.affinity.AffinityMapping;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAffinityMapping extends AffinityMapping<ConsumerAgentGroup, ConsumerAgentGroup> {

    @Override
    ConsumerAgentGroupAffinities get(ConsumerAgentGroup source);
}
