package de.unileipzig.irpact.v2.commons.awareness;

import de.unileipzig.irpact.v2.commons.distribution.Distribution;

/**
 * @author Daniel Abitz
 */
public interface AwarenessDistributionMapping<T, D> extends Distribution {

    void put(T item, D distribution);

    D getDistribution(T item);
}