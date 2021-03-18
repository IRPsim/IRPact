package de.unileipzig.irpact.core.spatial.eval;

import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialInformationDistanceFilter implements SpatialInformationDistanceFilter {

    protected SpatialModel model;
    protected SpatialInformation information;

    @Override
    public boolean test(SpatialInformation other) {
        return false;
    }
}
