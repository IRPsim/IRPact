package de.unileipzig.irpact.commons.graph2;

import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface Graph2<V, E> {

    //=========================
    // vertices
    //=========================

    boolean addVertex(V vertex);

    boolean hasVertex(V vertex);

    boolean removeVertex(V vertex);

    Stream<V> streamVertices();

    //=========================
    // edges
    //=========================

    boolean hasEdge(E edge);

    boolean removeEdge(E edge);

    Stream<E> streamEdges();
}
