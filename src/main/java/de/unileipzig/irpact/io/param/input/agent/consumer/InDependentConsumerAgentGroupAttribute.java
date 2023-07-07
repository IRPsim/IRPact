package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InDependentConsumerAgentGroupAttribute extends InConsumerAgentGroupAttribute {

    @TreeAnnotationResource.Init
    static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(LocalizedUiResource res) {
    }
}
