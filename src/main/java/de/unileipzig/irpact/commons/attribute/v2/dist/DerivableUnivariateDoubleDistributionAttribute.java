package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.DirectDerivable;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDoubleDistributionAttribute<T> extends UnivariateDoubleDistributionAttribute, DirectDerivable<T> {

    T derive(double value);

    @Override
    default T derive() {
        double value = getValue().drawDoubleValue();
        return derive(value);
    }
}
