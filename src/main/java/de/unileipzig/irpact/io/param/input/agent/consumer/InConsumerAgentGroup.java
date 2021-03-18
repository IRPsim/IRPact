package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Todo
@AddToRoot
@Definition
public interface InConsumerAgentGroup extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    int getNumberOfAgents();
}
