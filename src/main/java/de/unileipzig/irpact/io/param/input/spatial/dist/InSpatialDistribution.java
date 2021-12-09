package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InSpatialDistribution extends InIRPactEntity {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }
}
