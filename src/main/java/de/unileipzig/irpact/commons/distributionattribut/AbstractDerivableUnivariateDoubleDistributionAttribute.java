package de.unileipzig.irpact.commons.distributionattribut;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableUnivariateDoubleDistributionAttribute<T>
        extends UnivariateDoubleDistributionAttributeBase
        implements DerivableUnivariateDoubleDistributionAttribute<T> {

    @Override
    public T derive() {
        double value = drawDoubleValue();
        return derive(value);
    }
}
