package de.unileipzig.irpact.io.param.input.process.modular.ca;

import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.process.modular.InModularProcessModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public interface InConsumerAgentModularProcessModel extends InModularProcessModel {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }
}
