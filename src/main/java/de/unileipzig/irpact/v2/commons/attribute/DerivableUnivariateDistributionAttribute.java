package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distattr.UnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDistributionAttribute<T> extends UnivariateDoubleDistributionAttribute, DerivableAttribute<T> {

    T derive(double value);
}
