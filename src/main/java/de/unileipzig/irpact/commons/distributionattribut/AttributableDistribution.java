package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.distribution.Distribution;

/**
 * Combines attribute and distribution.
 *
 * @author Daniel Abitz
 */
public interface AttributableDistribution<T> extends Attribute, Distribution<T> {
}
