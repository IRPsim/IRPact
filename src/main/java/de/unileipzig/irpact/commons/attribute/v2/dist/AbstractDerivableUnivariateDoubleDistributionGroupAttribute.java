package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.attribute.v2.AbstractDerivableGenericValueGroupAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableUnivariateDoubleDistributionGroupAttribute<D>
        extends AbstractDerivableGenericValueGroupAttribute<D, UnivariateDoubleDistribution>
        implements DerivableUnivariateDoubleDistributionAttribute<D> {

    @Override
    public double drawDoubleValue() {
        return getValue().drawDoubleValue();
    }

    @Override
    public void setValue(Object value) {
        this.value = castTo(UnivariateDoubleDistribution.class, value);
    }
}
