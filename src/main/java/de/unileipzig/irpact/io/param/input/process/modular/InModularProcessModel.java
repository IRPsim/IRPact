package de.unileipzig.irpact.io.param.input.process.modular;

import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public interface InModularProcessModel extends InProcessModel {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }
}
