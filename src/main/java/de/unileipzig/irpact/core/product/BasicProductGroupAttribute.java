package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ProductAttribute> implements ProductGroupAttribute {

    protected int nextId = 0;

    public BasicProductGroupAttribute() {
    }

    @Override
    public BasicProductAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    protected synchronized int nextId() {
        int next = nextId;
        nextId++;
        return next;
    }

    @Override
    public BasicProductAttribute derive(double value) {
        return derive(getName() + "_" + nextId(), value);
    }

    @Override
    public BasicProductAttribute derive(String name, double value) {
        return new BasicProductAttribute(
                name,
                this,
                value
        );
    }
}
