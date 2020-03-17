package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.attribute.UnivariateDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttribute extends UnivariateDistributionAttribute
        implements ConsumerAgentGroupAttribute {

    public BasicConsumerAgentGroupAttribute(String name, UnivariateDistribution distribution) {
        super(name, distribution);
    }

    @Override
    public ConsumerAgentAttribute derive() {
        return derive(drawValue());
    }

    @Override
    public ConsumerAgentAttribute derive(double fixedValue) {
        return new BasicConsumerAgentAttribute(this, fixedValue);
    }
}
