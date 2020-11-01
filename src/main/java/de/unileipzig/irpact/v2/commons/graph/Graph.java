package de.unileipzig.irpact.v2.commons.graph;

import java.util.Set;
import java.util.stream.Stream;

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

    Stream<V> streamVertexes();

    Set<V> getNeighbours(V vertex);

    Stream<V> streamNeighbours(V source);

    boolean hasEdge(E edge);

    boolean removeEdge(E edge);

    int edgeCount();
}
