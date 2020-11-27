package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.commons.graph.Graph;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface GraphTopology<V, E> {

    void initalizeEdges(Graph<V, E> graph);
}
