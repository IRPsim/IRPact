package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.logging;

import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.InConsumerAgentEvalRAModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InConsumerAgentEvalRALoggingModule2 extends InConsumerAgentEvalRAModule2 {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }

    String getBaseName();
}
