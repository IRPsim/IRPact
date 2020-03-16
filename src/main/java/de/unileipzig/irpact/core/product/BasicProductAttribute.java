package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttribute implements ProductAttribute {

    private ProductGroupAttribute groupAttribute;
    private double value;

    public BasicProductAttribute(ProductGroupAttribute groupAttribute, double value) {
        this.groupAttribute = Check.requireNonNull(groupAttribute, "groupAttribute");
        this.value = value;
    }

    @Override
    public ProductGroupAttribute getGroup() {
        return groupAttribute;
    }

    @Override
    public BasicProductAttribute copy() {
        return new BasicProductAttribute(groupAttribute, value);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String getName() {
        return getGroup().getName();
    }
}
