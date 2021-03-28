package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distributionattribut.AbstractDerivableUnivariateDoubleDistributionAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttribute
        extends AbstractDerivableUnivariateDoubleDistributionAttribute<ConsumerAgentAttribute>
        implements ConsumerAgentGroupAttribute {

    protected boolean artificial;

    public BasicConsumerAgentGroupAttribute() {
    }

    @Override
    public BasicConsumerAgentGroupAttribute copyAttribute() {
        BasicConsumerAgentGroupAttribute copy = new BasicConsumerAgentGroupAttribute();
        copy.setName(name);
        copy.setUnivariateDoubleDistributionValue(distribution.copyDistribution());
        return copy;
    }

    @Override
    public boolean isArtificial() {
        return artificial;
    }

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
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
