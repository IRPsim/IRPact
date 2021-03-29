package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
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

    int inDegree(V vertex, T type);

    int outDegree(V vertex, T type);

    int degree(V vertex, T type);

    Set<V> getTargets(V from, T type);

    boolean getTargets(V from, T type, Collection<? super V> targets);

    Stream<V> streamTargets(V from, T type);

    Set<V> getAllTargets(V from);

    V getRandomTarget(V from, T type, Rnd rnd);

    boolean addEdge(V from, V to, T type, E edge);

    E setEdge(V from, V to, T type, E edge);

    boolean hasEdge(V from, V to, T type);

    boolean removeEdge(V from, V to, T type);

    Set<E> removeAllEdges(T type);

    E getEdge(V from, V to, T type);

    Map<T, E> getEdges(V from, V to);

    Collection<E> getAllEdges(T[] types);

    Set<E> getEdges(T type);

    Stream<E> streamEdgesFrom(V from, T type);

    Stream<E> streamEdgesTo(V to, T type);
}
