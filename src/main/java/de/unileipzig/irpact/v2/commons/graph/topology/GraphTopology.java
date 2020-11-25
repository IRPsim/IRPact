package de.unileipzig.irpact.v2.commons.graph.topology;

import de.unileipzig.irpact.v2.commons.graph.Graph;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface GraphTopology<V, E> {

    void initalizeEdges(Graph<V, E> graph);
}
