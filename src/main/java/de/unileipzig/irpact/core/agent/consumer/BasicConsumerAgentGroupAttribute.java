package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ConsumerAgentAttribute> implements ConsumerAgentGroupAttribute {

    public BasicConsumerAgentGroupAttribute() {
    }

    @Override
    public BasicConsumerAgentGroupAttribute copyAttribute() {
        BasicConsumerAgentGroupAttribute copy = new BasicConsumerAgentGroupAttribute();
        copy.setName(name);
        copy.setDistribution(distribution.copyDistribution());
        return copy;
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

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getValue().getHashCode()
        );
    }
}
