package de.unileipzig.irpact.v2.commons.graph;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public interface MultiGraph<V, E, T> extends Graph<V, E> {

    Set<V> getTargets(V from, T type);

    Stream<V> streamTargets(V from, T type);

    Set<V> getAllTargets(V from);

    boolean addEdge(V from, V to, T type, E edge);

    E setEdge(V from, V to, T type, E edge);

    boolean hasEdge(V from, V to, T type);

    boolean removeEdge(V from, V to, T type);

    Set<E> removeAllEdges(T type);

    E getEdge(V from, V to, T type);

    Map<T, E> getEdges(V from, V to);

    Set<E> getEdges(T type);
}
