package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval;

import de.unileipzig.irpact.io.param.input.process2.modular.InModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InConsumerAgentReevaluationModule2 extends InModule2 {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }
}
