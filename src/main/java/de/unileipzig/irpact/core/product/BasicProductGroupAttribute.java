package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.distributionattribut.AbstractDerivableUnivariateDoubleDistributionAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttribute extends AbstractDerivableUnivariateDoubleDistributionAttribute<ProductAttribute> implements ProductGroupAttribute {

    protected int nextId = 0;

    public BasicProductGroupAttribute() {
    }

    @Override
    public ProductDoubleAttribute derive() {
        double value = drawDoubleValue();
        return derive(value);
    }

    protected synchronized int nextId() {
        int next = nextId;
        nextId++;
        return next;
    }

    @Override
    public ProductDoubleAttribute derive(double value) {
        return derive(getName() + "_" + nextId(), value);
    }

    @Override
    public ProductDoubleAttribute derive(String name, double value) {
        return new ProductDoubleAttribute(
                name,
                this,
                value
        );
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                nextId,
                getValue().getChecksum()
        );
    }
}
