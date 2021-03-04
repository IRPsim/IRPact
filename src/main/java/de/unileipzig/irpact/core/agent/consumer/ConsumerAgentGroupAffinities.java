package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.affinity.Affinities;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAffinities extends Affinities<ConsumerAgentGroup> {

    @Override
    ConsumerAgentGroupAffinities without(ConsumerAgentGroup target);
}
