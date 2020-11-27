package de.unileipzig.irpact.core.agent.consumer;


import de.unileipzig.irpact.commons.affinity.AffinityMapping;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAffinityMapping extends AffinityMapping<ConsumerAgentGroup, ConsumerAgentGroup> {

    @Override
    ConsumerAgentGroupAffinities get(ConsumerAgentGroup source);
}
