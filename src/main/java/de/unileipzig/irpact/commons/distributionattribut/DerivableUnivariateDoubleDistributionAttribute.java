package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.DerivableValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDoubleDistributionAttribute<T> extends UnivariateDoubleDistributionAttribute, DerivableValueAttribute<T> {

    T derive(double value);
}
