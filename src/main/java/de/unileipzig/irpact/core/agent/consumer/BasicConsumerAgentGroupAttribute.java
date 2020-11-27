package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ConsumerAgentAttribute> implements ConsumerAgentGroupAttribute {

    public BasicConsumerAgentGroupAttribute() {
    }

    @Override
    public BasicConsumerAgentAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    @Override
    public BasicConsumerAgentAttribute derive(double value) {
        return new BasicConsumerAgentAttribute(getName(), this, value);
    }
}
