package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InSpatialDistribution {

    SpatialDistribution getInstance();
}