package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.affinity.Affinities;
import de.unileipzig.irpact.commons.affinity.BasicAffinities;

import java.util.Map;
import java.util.Objects;

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

    @Override
    protected BasicBasicConsumerAgentGroupAffinities newInstance() {
        return new BasicBasicConsumerAgentGroupAffinities();
    }

    @Override
    public BasicBasicConsumerAgentGroupAffinities without(ConsumerAgentGroup target) {
        return (BasicBasicConsumerAgentGroupAffinities) super.without(target);
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                IsEquals.getMapHashCode(values)
        );
    }
}
