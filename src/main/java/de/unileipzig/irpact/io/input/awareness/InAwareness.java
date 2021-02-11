package de.unileipzig.irpact.io.input.awareness;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InAwareness {

    static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Awareness")
                .setEdnPriority(2)
                .putCache("Awareness");
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    <T> Awareness<T> createInstance();
}
