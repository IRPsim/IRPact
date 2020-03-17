package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.affinity.AffinitiesMapping;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAffinitiesMapping extends AffinitiesMapping<ConsumerAgentGroup, ConsumerAgentGroup> {

    @Override
    ConsumerAgentGroupAffinities get(ConsumerAgentGroup source);
}
