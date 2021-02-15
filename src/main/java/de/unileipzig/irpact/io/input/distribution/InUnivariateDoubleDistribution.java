package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InUnivariateDoubleDistribution {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getName();

    UnivariateDoubleDistribution getInstance(Rnd masterRnd);
}
