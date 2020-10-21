package de.unileipzig.irpact.v2.commons.graph;

import java.util.Set;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface Graph<V, E> {

    boolean addVertex(V vertex);

    boolean hasVertex(V vertex);

    boolean removeVertex(V vertex);

    int vertexCount();

    Set<V> vertexSet();

    Set<V> getNeighbours(V vertex);

    boolean hasEdge(E edge);

    boolean removeEdge(E edge);

    int edgeCount();
}
