package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.attribute.AbstractUnivariateDistributionAttributeGroup;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttribute extends AbstractUnivariateDistributionAttributeGroup<ConsumerAgentAttribute> implements ConsumerAgentGroupAttribute {

    public BasicConsumerAgentGroupAttribute() {
    }

    @Override
    public BasicConsumerAgentAttribute derive() {
        double value = drawValue();
        return derive(value);
    }

    @Override
    public BasicConsumerAgentAttribute derive(double value) {
        return new BasicConsumerAgentAttribute(getName(), this, value);
    }
}
