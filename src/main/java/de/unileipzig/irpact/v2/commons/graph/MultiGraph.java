package de.unileipzig.irpact.v2.commons.graph;

import java.util.Map;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public interface MultiGraph<V, E, T> extends Graph<V, E> {

    boolean addEdge(V from, V to, T type, E edge);

    E setEdge(V from, V to, T type, E edge);

    boolean hasEdge(V from, V to, T type);

    boolean removeEdge(V from, V to, T type);

    E getEdge(V from, V to, T type);

    Map<T, E> getEdges(V from, V to);
}
