package de.unileipzig.irpact.io.param.input.color;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.awt.*;
import java.util.Comparator;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InColor extends InIRPactEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    Comparator<InColor> COMPARE_PRIORITY = Comparator.comparingInt(InColor::getPriority);

    int getPriority();

    Color toColor();
}
