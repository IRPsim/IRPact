package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;
import de.unileipzig.irpact.commons.distribution.Distribution;

/**
 * Combines attribute and distribution.
 *
 * @author Daniel Abitz
 */
public interface DistributionAttribute<T> extends ValueAttribute, Distribution<T> {
}
