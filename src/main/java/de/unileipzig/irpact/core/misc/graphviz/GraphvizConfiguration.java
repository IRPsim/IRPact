package de.unileipzig.irpact.core.misc.graphviz;

import de.unileipzig.irpact.core.network.SocialGraph;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface GraphvizConfiguration {

    void printSocialGraph(SocialGraph graph, SocialGraph.Type edgeType) throws Exception;

//    void printSocialGraph(
//            SocialGraph graph,
//            SocialGraph.Type edgeType,
//            Path outputFile,
//            Path dotFile) throws Exception;
}
