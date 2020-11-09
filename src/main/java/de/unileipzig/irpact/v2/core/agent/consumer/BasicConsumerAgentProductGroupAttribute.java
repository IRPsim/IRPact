package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttributeGroupEntityBase;
import de.unileipzig.irpact.v2.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentProductGroupAttribute extends DoubleAttributeGroupEntityBase<ConsumerAgentGroupProductGroupAttribute> implements ConsumerAgentProductGroupAttribute {

    protected ProductGroup productGroup;

    public BasicConsumerAgentProductGroupAttribute() {
    }

    public BasicConsumerAgentProductGroupAttribute(String name, ConsumerAgentGroupProductGroupAttribute groupAttribute, ProductGroup productGroup, double value) {
        setName(name);
        setGroup(groupAttribute);
        setProductGroup(productGroup);
        setDoubleValue(value);
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    @Override
    public ProductGroup getProductGroup() {
        return productGroup;
    }
}
