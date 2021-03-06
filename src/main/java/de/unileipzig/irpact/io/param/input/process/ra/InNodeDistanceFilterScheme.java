package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.core.network.filter.NodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.InNodeFilterScheme;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InNodeDistanceFilterScheme extends InNodeFilterScheme {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }

    NodeDistanceFilterScheme createScheme();
}
