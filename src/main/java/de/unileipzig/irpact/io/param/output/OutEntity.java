package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface OutEntity extends Copyable {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getName();
}
