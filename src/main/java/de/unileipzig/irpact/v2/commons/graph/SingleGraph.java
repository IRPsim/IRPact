package de.unileipzig.irpact.v2.commons.graph;

import java.util.Set;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public interface SingleGraph<V, E> extends Graph<V, E> {

    boolean addEdge(V from, V to, E edge);

    E setEdge(V from, V to, E edge);

    boolean hasEdge(V from, V to);

    boolean removeEdge(V from, V to);

    E getEdge(V from, V to);

    Set<E> getEdges();
}
