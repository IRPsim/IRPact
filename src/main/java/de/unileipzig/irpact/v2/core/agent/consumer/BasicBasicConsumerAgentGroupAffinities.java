package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.affinity.BasicAffinities;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicBasicConsumerAgentGroupAffinities extends BasicAffinities<ConsumerAgentGroup> implements ConsumerAgentGroupAffinities {

    public BasicBasicConsumerAgentGroupAffinities() {
        super();
    }

    public BasicBasicConsumerAgentGroupAffinities(Map<ConsumerAgentGroup, Double> values) {
        super(values);
    }
}
