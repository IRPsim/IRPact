package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ProductAttribute> implements ProductGroupAttribute {

    protected int id = 0;

    public BasicProductGroupAttribute() {
    }

    @Override
    public BasicProductAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    protected synchronized int nextId() {
        int next = id;
        id++;
        return next;
    }

    @Override
    public BasicProductAttribute derive(double value) {
        return new BasicProductAttribute(
                getName() + "_" + nextId(),
                this,
                value
        );
    }
}
