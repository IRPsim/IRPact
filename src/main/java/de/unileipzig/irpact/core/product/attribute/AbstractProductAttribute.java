package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.v3.AbstractAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractProductAttribute extends AbstractAttribute implements ProductAttribute {

    protected ProductGroupAttribute group;

    @Override
    public abstract AbstractProductAttribute copy();

    @Override
    public ProductGroupAttribute getGroup() {
        return group;
    }

    public void setGroup(ProductGroupAttribute group) {
        this.group = group;
    }
}
