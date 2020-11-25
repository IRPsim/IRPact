package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;
import de.unileipzig.irpact.v2.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupProductGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ConsumerAgentProductGroupAttribute> implements ConsumerAgentGroupProductGroupAttribute {

    protected ProductGroup productGroup;

    public BasicConsumerAgentGroupProductGroupAttribute() {
    }

    @Override
    public BasicConsumerAgentProductGroupAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    @Override
    public BasicConsumerAgentProductGroupAttribute derive(double value) {
        return new BasicConsumerAgentProductGroupAttribute(getName(), this, getProductGroup(), value);
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    @Override
    public ProductGroup getProductGroup() {
        return productGroup;
    }
}
