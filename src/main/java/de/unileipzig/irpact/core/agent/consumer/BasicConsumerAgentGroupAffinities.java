package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.affinity.BasicAffinities;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinities extends BasicAffinities<ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinities {

    public BasicConsumerAgentGroupAffinities(Map<ConsumerAgentGroup, Double> values) {
        super(values);
    }
}
