package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.attribute.DerivableUnivariateDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableUnivariateDoubleDistributionAttribute<T>
        extends UnivariateDoubleDistributionAttributeBase
        implements DerivableUnivariateDistributionAttribute<T> {

    @Override
    public T derive() {
        double value = drawDoubleValue();
        return derive(value);
    }
}