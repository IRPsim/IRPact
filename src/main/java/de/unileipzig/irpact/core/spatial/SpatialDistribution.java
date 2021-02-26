package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public interface SpatialDistribution extends UnivariateDistribution<SpatialInformation> {

    default boolean isShareble(SpatialDistribution target) {
        return false;
    }

    /**
     * Adds complex data to the target distribution. This method is primarly used in the
     * restoration process.
     *
     * @param target target Distribution
     */
    default void addComplexDataTo(SpatialDistribution target) {
    }
}
