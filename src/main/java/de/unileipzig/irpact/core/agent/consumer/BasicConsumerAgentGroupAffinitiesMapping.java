package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.affinity.Affinities;
import de.unileipzig.irpact.core.affinity.BasicAffinitiesMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinitiesMapping
        extends BasicAffinitiesMapping<ConsumerAgentGroup, ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinitiesMapping {

    public BasicConsumerAgentGroupAffinitiesMapping() {
        this(new HashMap<>());
    }

    public BasicConsumerAgentGroupAffinitiesMapping(Map<ConsumerAgentGroup, Affinities<ConsumerAgentGroup>> consumerAgentGroupAffinitiesMap) {
        super(consumerAgentGroupAffinitiesMap);
    }

    @Override
    public ConsumerAgentGroupAffinities get(ConsumerAgentGroup source) {
        return (ConsumerAgentGroupAffinities) super.get(source);
    }
}
