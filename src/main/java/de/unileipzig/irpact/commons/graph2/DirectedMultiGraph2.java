package de.unileipzig.irpact.commons.graph2;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public interface DirectedMultiGraph2<V, E, T> extends MultiGraph2<V, E, T> {

    //=========================
    // targets
    //=========================

    Stream<V> streamTargets(T type, V from);

    V getRandomTarget(T type, V from, Rnd rnd);

    //=========================
    // sources
    //=========================

    Stream<V> streamSources(T type, V to);

    V getRandomSource(T type, V to, Rnd rnd);

    //=========================
    // sources and targets
    //=========================

    Stream<V> streamNeighbours(T type, V vertex);

    //=========================
    // edges
    //=========================

    boolean addEdge(T type, V from, V to, E edge);

    E setEdge(T type, V from, V to, E edge);

    boolean hasEdge(T type, V from, V to);

    boolean removeEdge(T type, V from, V to);

    E getEdge(T type, V from, V to);
}
