package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.attribute.v2.AbstractDerivableGenericValueGroupAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableUnivariateDoubleDistributionGroupAttribute<D>
        extends AbstractDerivableGenericValueGroupAttribute<D, UnivariateDoubleDistributionAttribute>
        implements DerivableUnivariateDoubleDistributionAttribute<D> {

    @Override
    protected Class<UnivariateDoubleDistributionAttribute> getTClass() {
        return UnivariateDoubleDistributionAttribute.class;
    }

    @Override
    public double drawDoubleValue() {
        return getValue().drawDoubleValue();
    }
}
