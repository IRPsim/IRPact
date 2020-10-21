package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.commons.attribute.AbstractUnivariateDistributionAttributeGroup;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttribute extends AbstractUnivariateDistributionAttributeGroup<ProductAttribute> implements ProductGroupAttribute {

    public BasicProductGroupAttribute() {
    }

    @Override
    public BasicProductAttribute derive() {
        double value = drawValue();
        return derive(value);
    }

    @Override
    public BasicProductAttribute derive(double value) {
        return new BasicProductAttribute(getName(), this, value);
    }
}
