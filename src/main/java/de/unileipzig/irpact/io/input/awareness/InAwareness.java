package de.unileipzig.irpact.io.input.awareness;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.io.spec.SpecificationManager;
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
                .setEdnDescription("Legt die verwendeten Awareness-Varianten für die Konsumergruppen fest.")
                .putCache("Awareness");
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getName();

    <T> Awareness<T> createInstance();
}
