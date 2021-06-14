package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.develop.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("restore-zeug rausnehmen und in ein eigenes interface packen")
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

    //=========================
    //v2
    //=========================

    default boolean setUsed(SpatialInformation information) {
        throw new UnsupportedOperationException();
    }
}
