package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.DerivableAttribute;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDoubleDistributionAttribute<T> extends AttributableUnivariateDoubleDistribution, DerivableAttribute<T> {

    T derive(double value);
}
