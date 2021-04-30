package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AbstractRelatedGroupAttribute;
import de.unileipzig.irpact.commons.distributionattribut.AbstractDerivableUnivariateDoubleDistributionAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicProductRelatedConsumerAgentGroupAttribute2
        extends AbstractRelatedGroupAttribute<ProductGroup>
        implements ConsumerAgentGroupRelatedAttribute<ProductGroup> {

    public BasicProductRelatedConsumerAgentGroupAttribute2() {
    }

    @Override
    public BasicProductRelatedConsumerAgentGroupAttribute2 copy() {
        BasicProductRelatedConsumerAgentGroupAttribute2 copy = new BasicProductRelatedConsumerAgentGroupAttribute2();
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
}
