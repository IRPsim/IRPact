package de.unileipzig.irpact.v2.commons.attribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUnivariateDistributionAttributeGroup<T> extends UnivariateDistributionAttributeBase implements UnivariateDistributionAttributeGroup<T> {

    @Override
    public T derive() {
        double value = drawValue();
        return derive(value);
    }
}
