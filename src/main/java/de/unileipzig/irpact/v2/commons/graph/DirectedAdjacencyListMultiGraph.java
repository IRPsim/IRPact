package de.unileipzig.irpact.v2.commons.graph;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public class DirectedAdjacencyListMultiGraph<V, E, T> implements DirectedMultiGraph<V, E, T> {

    protected Supplier<? extends Map<V, Map<V, Map<T, E>>>> adjacencyMapSupplier;
    protected Function<? super V, ? extends Map<V, Map<T, E>>> map0Function;
    protected Function<? super V, ? extends Map<T, E>> map1Function;

    protected Map<V, Map<V, Map<T, E>>> adjacencyMap;

    public DirectedAdjacencyListMultiGraph() {
        this(LinkedHashMap::new, v -> new LinkedHashMap<>(), v -> new LinkedHashMap<>());
    }

    public DirectedAdjacencyListMultiGraph(
            Supplier<? extends Map<V, Map<V, Map<T, E>>>> adjacencyMapSupplier,
            Function<? super V, ? extends Map<V, Map<T, E>>> map0Function,
            Function<? super V, ? extends Map<T, E>> map1Function) {
        this.adjacencyMapSupplier = adjacencyMapSupplier;
        this.map0Function = map0Function;
        this.map1Function = map1Function;
        this.adjacencyMap = adjacencyMapSupplier.get();
    }

    @Override
    public boolean addVertex(V vertex) {
        if(hasVertex(vertex)) {
            return false;
        } else {
            Map<V, Map<T, E>> map0 = map0Function.apply(vertex);
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
            for(Map<V, Map<T, E>> map0: adjacencyMap.values()) {
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
    public Set<V> getVertices() {
        return adjacencyMap.keySet();
    }

    @Override
    public Stream<V> streamVertices() {
        return adjacencyMap.keySet().stream();
    }

    @Override
    public boolean hasEdge(E edge) {
        for(Map<V, Map<T, E>> map0: adjacencyMap.values()) {
            for(Map<T, E> map1: map0.values()) {
                if(map1.containsValue(edge)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeEdge(E edge) {
        for(Map.Entry<V, Map<V, Map<T, E>>> entry0: adjacencyMap.entrySet()) {
            for(Map.Entry<V, Map<T, E>> entry1: entry0.getValue().entrySet()) {
                for(Map.Entry<T, E> entry2: entry1.getValue().entrySet()) {
                    if(Objects.equals(entry2.getValue(), edge)) {
                        V from = entry0.getKey();
                        V to = entry1.getKey();
                        T type = entry2.getKey();
                        return removeEdge(from, to, type);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int edgeCount() {
        int total = 0;
        for(Map<V, Map<T, E>> map0: adjacencyMap.values()) {
            for(Map<T, E> map1: map0.values()) {
                total += map1.size();
            }
        }
        return total;
    }

    @Override
    public Set<V> getTargets(V from, T type) {
        Set<V> targets = new LinkedHashSet<>();
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        if(map0 == null) return targets;
        for(Map.Entry<V, Map<T, E>> map1Entry: map0.entrySet()) {
            if(map1Entry.getValue().containsKey(type)) {
                targets.add(map1Entry.getKey());
            }
        }
        return targets;
    }

    @Override
    public Stream<V> streamTargets(V from, T type) {
        return getTargets(from, type).stream();
    }

    @Override
    public Set<V> getAllTargets(V vertex) {
        Map<V, Map<T, E>> map0 = adjacencyMap.get(vertex);
        return map0 == null ? Collections.emptySet() : map0.keySet();
    }

    @Override
    public boolean addEdge(V from, V to, T type, E edge) {
        if(hasEdge(from, to, type)) {
            return false;
        } else {
            setEdge(from, to, type, edge);
            return true;
        }
    }

    @Override
    public E setEdge(V from, V to, T type, E edge) {
        addVertex(from);
        addVertex(to);
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        Map<T, E> map1 = map0.computeIfAbsent(to, _to -> map1Function.apply(_to));
        return map1.put(type, edge);
    }

    @Override
    public boolean hasEdge(V from, V to, T type) {
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        if(map0 == null) return false;
        Map<T, E> map1 = map0.get(to);
        if(map1 == null) return false;
        return map1.get(type) != null;
    }

    @Override
    public boolean removeEdge(V from, V to, T type) {
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        if(map0 == null) return false;
        Map<T, E> map1 = map0.get(to);
        if(map1 == null) return false;
        return map1.remove(type) != null;
    }

    @Override
    public Set<E> removeAllEdges(T type) {
        Set<E> removed = new LinkedHashSet<>();
        for(Map<V, Map<T, E>> map0: adjacencyMap.values()) {
            for(Map<T, E> map1: map0.values()) {
                if(map1.containsKey(type)) {
                    removed.add(map1.remove(type));
                }
            }
        }
        return removed;
    }

    @Override
    public E getEdge(V from, V to, T type) {
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        if(map0 == null) return null;
        Map<T, E> map1 = map0.get(to);
        if(map1 == null) return null;
        return map1.get(type);
    }

    @Override
    public Map<T, E> getEdges(V from, V to) {
        Map<V, Map<T, E>> map0 = adjacencyMap.get(from);
        if(map0 == null) return Collections.emptyMap();
        Map<T, E> map1 = map0.get(to);
        return map1 == null ? Collections.emptyMap() : map1;
    }

    @Override
    public Set<E> getEdges(T type) {
        Set<E> edges = new LinkedHashSet<>();
        for(Map<V, Map<T, E>> map0: adjacencyMap.values()) {
            for(Map<T, E> map1: map0.values()) {
                if(map1.containsKey(type)) {
                    edges.add(map1.get(type));
                }
            }
        }
        return edges;
    }

    public Stream<E> streamEdges() {
        return adjacencyMap.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.entrySet().stream())
                .map(Map.Entry::getValue);
    }

    public Stream<E> streamEdges(T type) {
        return adjacencyMap.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.entrySet().stream())
                .filter(e -> e.getKey() == type)
                .map(Map.Entry::getValue);
    }
}
