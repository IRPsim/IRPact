package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InGraphTopologyScheme {

    static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Topologie")
                .setEdnPriority(0)
                .putCache("Topologie");
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
