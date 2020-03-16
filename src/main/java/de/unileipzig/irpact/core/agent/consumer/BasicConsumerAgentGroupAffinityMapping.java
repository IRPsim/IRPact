package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.affinity.BasicUnaryAffinityMapping;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinityMapping extends BasicUnaryAffinityMapping<ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinityMapping {

    public BasicConsumerAgentGroupAffinityMapping(Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> mapping) {
        super(mapping);
    }
}
