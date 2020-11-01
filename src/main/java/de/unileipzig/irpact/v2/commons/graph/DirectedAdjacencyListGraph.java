package de.unileipzig.irpact.v2.commons.graph;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @author Daniel Abitz
 */
public class DirectedAdjacencyListGraph<V, E> implements DirectedGraph<V, E> {

    protected Supplier<? extends Map<V, Map<V, E>>> adjacencyMapSupplier;
    protected Function<? super V, ? extends Map<V, E>> map0Function;

    protected Map<V, Map<V, E>> adjacencyMap;

    public DirectedAdjacencyListGraph() {
        this(LinkedHashMap::new, v -> new LinkedHashMap<>());
    }

    public DirectedAdjacencyListGraph(
            Supplier<? extends Map<V, Map<V, E>>> adjacencyMapSupplier,
            Function<? super V, ? extends Map<V, E>> map0Function) {
        this.adjacencyMapSupplier = adjacencyMapSupplier;
        this.map0Function = map0Function;
        this.adjacencyMap = adjacencyMapSupplier.get();
    }

    @Override
    public boolean addVertex(V vertex) {
        if(hasVertex(vertex)) {
            return false;
        } else {
            Map<V, E> map0 = map0Function.apply(vertex);
            adjacencyMap.put(vertex, map0);
            return true;
        }
    }

    @Override
    public boolean hasVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {
        if(hasVertex(vertex)) {
            adjacencyMap.remove(vertex);
            for(Map<V, E> map0: adjacencyMap.values()) {
                map0.remove(vertex);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int vertexCount() {
        return adjacencyMap.size();
    }

    @Override
    public Set<V> vertexSet() {
        return adjacencyMap.keySet();
    }

    @Override
    public Stream<V> streamVertexes() {
        return adjacencyMap.keySet().stream();
    }

    @Override
    public Set<V> getNeighbours(V vertex) {
        Map<V, E> map0 = adjacencyMap.get(vertex);
        return map0 == null ? Collections.emptySet() : map0.keySet();
    }

    @Override
    public Stream<V> streamNeighbours(V source) {
        return getNeighbours(source).stream();
    }

    @Override
    public boolean hasEdge(E edge) {
        for(Map<V, E> map0: adjacencyMap.values()) {
            if(map0.containsValue(edge)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeEdge(E edge) {
        for(Map.Entry<V, Map<V, E>> entry0: adjacencyMap.entrySet()) {
            for(Map.Entry<V, E> entry1: entry0.getValue().entrySet()) {
                if(Objects.equals(entry1.getValue(), edge)) {
                    V from = entry0.getKey();
                    V to = entry1.getKey();
                    return removeEdge(from, to);
                }
            }
        }
        return false;
    }

    @Override
    public int edgeCount() {
        int total = 0;
        for(Map<V, E> map0: adjacencyMap.values()) {
            total += map0.size();
        }
        return total;
    }

    @Override
    public boolean addEdge(V from, V to, E edge) {
        if(hasEdge(from, to)) {
            return false;
        } else {
            setEdge(from, to, edge);
            return true;
        }
    }

    @Override
    public E setEdge(V from, V to, E edge) {
        addVertex(from);
        addVertex(to);
        Map<V, E> map0 = adjacencyMap.get(from);
        return map0.put(to, edge);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        Map<V, E> map0 = adjacencyMap.get(from);
        if(map0 == null) return false;
        return map0.get(to) != null;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        Map<V, E> map0 = adjacencyMap.get(from);
        if(map0 == null) return false;
        return map0.remove(to) != null;
    }

    @Override
    public E getEdge(V from, V to) {
        Map<V, E> map0 = adjacencyMap.get(from);
        if(map0 == null) return null;
        return map0.get(to);
    }
}
