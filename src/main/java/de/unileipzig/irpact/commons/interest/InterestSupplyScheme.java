package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.core.misc.Scheme;

/**
 * @author Daniel Abitz
 */
public interface InterestSupplyScheme<T> extends Scheme {

    Interest<T> derive();
}
