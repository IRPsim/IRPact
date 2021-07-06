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

    //=========================
    // targets
    //=========================

    Set<V> getTargets(V from, T type);

    boolean getTargets(V from, T type, Collection<? super V> targets);

    Stream<V> streamTargets(V from, T type);

    Set<V> getAllTargets(V from);

    V getRandomTarget(V from, T type, Rnd rnd);

    //=========================
    // sources
    //=========================

    Set<V> getSources(V to, T type);

    boolean getSources(V to, T type, Collection<? super V> sources);

    Stream<V> streamSources(V to, T type);

    Set<V> getAllSources(V to);

    //=========================
    // sources and targets
    //=========================

    Set<V> getSourcesAndTargets(V fromOrTo, T type);

    boolean getSourcesAndTargets(V fromOrTo, T type, Collection<? super V> sourcesAndTargets);

    Stream<V> streamSourcesAndTargets(V fromOrTo, T type);

    Set<V> getAllSourcesAndTargets(V fromOrTo);

    //=========================
    // edges
    //=========================

    boolean addEdge(V from, V to, T type, E edge);

    E setEdge(V from, V to, T type, E edge);

    boolean hasEdge(V from, V to, T type);

    boolean removeEdge(V from, V to, T type);

    Set<E> removeAllEdges(T type);

    E getEdge(V from, V to, T type);

    Map<T, E> getEdges(V from, V to);

    Collection<E> getAllEdges();

    Collection<E> getAllEdges(T[] types);

    Stream<E> streamAllEdges();

    Stream<E> streamAllEdges(T[] types);

    Set<E> getEdges(T type);

    Stream<E> streamEdgesFrom(V from, T type);

    Stream<E> streamEdgesTo(V to, T type);
}
