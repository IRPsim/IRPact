package de.unileipzig.irpact.io.param.input.process.modular.ca.component;

import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public interface InConsumerAgentCalculationModule extends InConsumerAgentModule {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }
}
