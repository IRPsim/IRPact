package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InSpatialDistribution {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    SpatialDistribution getInstance();
}
