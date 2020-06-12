package de.unileipzig.irpact.experimental.todev.graph;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Schnittstelle fuer gerichtete Graphen mit definierten Kantengewichten.
 *
 * @param <V> Vertextyp, null ist erlaubt und wird bei Aufrufen beruecksichtigt
 * @param <W> Typ der Kantengewichte, null ist erlaubt und wird bei Aufrufen beruecksichtigt
 * @author Daniel Abitz
 */
@ToDevelop
public interface DirectedGraph<V, W> {

    DirectedGraph<V, W> copy();

    int vertexCount();

    void addVertex(V vertex);

    boolean hasVertex(V vertex);

    boolean removeVertex(V vertex);

    void forEachNode(Consumer<? super V> consumer);

    int edgeCount();

    boolean addEdge(V from, V to, W weight);

    W setEdge(V from, V to, W weight);

    boolean hasEdge(V from, V to);

    void changeWeight(V from, V to, W weight) throws NoSuchElementException;

    W getWeight(V from, V to);

    W removeEdge(V from, V to);

    void forEachEdge(EdgeConsumer<? super V, ? super W> consumer);

    Iterator<V> iterateNeighbours(V from);

    Iterator<V> iterateNeighbours(V from, boolean directed, boolean distinct);

    Iterable<V> iterableNeighbours(V from);

    Iterable<V> iterableNeighbours(V from, boolean directed, boolean distinct);

    Stream<V> streamNeighbours(V from);

    Stream<V> streamNeighbours(V from, boolean directed, boolean distinct);

    @SuppressWarnings("unchecked")
    boolean hasPath(V... path);

    boolean hasPath(Collection<? extends V> path);
}
