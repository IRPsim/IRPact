package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.attribute.DerivableDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDistributionAttribute<T> extends AttributableUnivariateDoubleDistribution, DerivableDistributionAttribute<T> {

    T derive(double value);
}
