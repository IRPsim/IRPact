package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.attribute.v2.ValueAttribute;
import de.unileipzig.irpact.commons.distribution.Distribution;

/**
 * Combines attribute and distribution.
 *
 * @author Daniel Abitz
 */
public interface DistributionAttribute<T> extends ValueAttribute, Distribution<T> {
}
