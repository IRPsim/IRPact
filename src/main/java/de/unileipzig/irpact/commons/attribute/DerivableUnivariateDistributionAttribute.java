package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.Derivable;
import de.unileipzig.irpact.commons.distattr.UnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface DerivableUnivariateDistributionAttribute<T> extends UnivariateDoubleDistributionAttribute, Derivable<T> {

    T derive(double value);
}
