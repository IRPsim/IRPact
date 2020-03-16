package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.affinity.BasicSameTypeAffinities;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinities extends BasicSameTypeAffinities<ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinities {

    public BasicConsumerAgentGroupAffinities(Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> mapping) {
        super(mapping);
    }
}
