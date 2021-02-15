package de.unileipzig.irpact.core.misc.graphviz;

import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public interface GraphvizConfiguration {

    void printSocialGraph(SocialGraph graph, SocialGraph.Type edgeType) throws Exception;
}
