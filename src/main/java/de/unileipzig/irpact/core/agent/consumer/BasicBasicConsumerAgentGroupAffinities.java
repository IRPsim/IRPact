package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.affinity.BasicAffinities;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;

/**
 * @author Daniel Abitz
 */
public class BasicBasicConsumerAgentGroupAffinities extends BasicAffinities<ConsumerAgentGroup> implements ConsumerAgentGroupAffinities {

    public BasicBasicConsumerAgentGroupAffinities() {
        super();
    }

    public BasicBasicConsumerAgentGroupAffinities(WeightedMapping<ConsumerAgentGroup> mapping) {
        super(mapping);
    }

    @Override
    protected BasicBasicConsumerAgentGroupAffinities newInstance(WeightedMapping<ConsumerAgentGroup> copy) {
        return new BasicBasicConsumerAgentGroupAffinities(copy);
    }

    @Override
    public BasicBasicConsumerAgentGroupAffinities createWithout(ConsumerAgentGroup target) {
        return (BasicBasicConsumerAgentGroupAffinities) super.createWithout(target);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getCollChecksum(mapping.elements());
    }
}
