package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.distribution.DistributionBase;

/**
 * @author Daniel Abitz
 */
public interface AwarenessDistributionMapping<T, D> extends DistributionBase {

    void put(T item, D distribution);

    D getDistribution(T item);
}
