package de.unileipzig.irpact.v2.commons.graph;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface SingleGraph<V, E> extends Graph<V, E> {

    int inDegree(V vertex);

    int outDegree(V vertex);

    int degree(V vertex);

    Set<V> getTargets(V from);

    boolean addEdge(V from, V to, E edge);

    E setEdge(V from, V to, E edge);

    boolean hasEdge(V from, V to);

    boolean removeEdge(V from, V to);

    E getEdge(V from, V to);

    Set<E> getEdges();

    Stream<E> streamEdgesFrom(V from);

    Stream<E> streamEdgesTo(V to);
}
