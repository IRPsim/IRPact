package de.unileipzig.irpact.io.param.input.process.modular;

import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InModularProcessModel extends InProcessModel {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
