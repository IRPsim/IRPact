package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InConsumerAgentGroup extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    void setSpatialDistribution(InSpatialDistribution distribution);
}
