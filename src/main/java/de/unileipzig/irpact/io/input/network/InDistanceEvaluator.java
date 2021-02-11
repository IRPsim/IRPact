package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InDistanceEvaluator {

    static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Abstandsfunktion")
                .setEdnPriority(1)
                .putCache("Abstandsfunktion");
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    DistanceEvaluator getInstance();
}
