package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.distributionattribut.AbstractDerivableUnivariateDoubleDistributionAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupValueAttribute
        extends AbstractDerivableUnivariateDoubleDistributionAttribute<ConsumerAgentAttribute>
        implements ConsumerAgentGroupValueAttribute {

    public BasicConsumerAgentGroupValueAttribute() {
    }

    @Override
    public BasicConsumerAgentGroupValueAttribute copy() {
        BasicConsumerAgentGroupValueAttribute copy = new BasicConsumerAgentGroupValueAttribute();
        copy.setName(name);
        copy.setUnivariateDoubleDistributionValue(distribution.copyDistribution());
        return copy;
    }

    @Override
    public ConsumerAgentDoubleAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    @Override
    public ConsumerAgentDoubleAttribute derive(double value) {
        return new ConsumerAgentDoubleAttribute(getName(), this, isArtificial(), value);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                isArtificial(),
                getValue().getChecksum()
        );
    }
}
