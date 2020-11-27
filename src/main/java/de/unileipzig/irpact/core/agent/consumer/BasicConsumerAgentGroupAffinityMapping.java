package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.affinity.Affinities;
import de.unileipzig.irpact.commons.affinity.BasicAffinityMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinityMapping
        extends BasicAffinityMapping<ConsumerAgentGroup, ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinityMapping {

    public BasicConsumerAgentGroupAffinityMapping() {
        this(new HashMap<>());
    }

    public BasicConsumerAgentGroupAffinityMapping(Map<ConsumerAgentGroup, Affinities<ConsumerAgentGroup>> mapping) {
        this.mapping = mapping;
    }

    @Override
    protected ConsumerAgentGroupAffinities newAffinitiesInstance() {
        return new BasicBasicConsumerAgentGroupAffinities();
    }

    @Override
    public ConsumerAgentGroupAffinities get(ConsumerAgentGroup source) {
        return (ConsumerAgentGroupAffinities) super.get(source);
    }
}
