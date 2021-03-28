package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.util.CollectionUtil;

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
public class FastDirectedMultiGraph<V, E, T> implements DirectedMultiGraph<V, E, T> {

    protected Supplier<? extends Map<V, Map<T, Map<V, E>>>> mapSupplier;
    protected Function<? super V, ? extends Map<T, Map<V, E>>> map0Supplier;
    protected Function<? super T, ? extends Map<V, E>> map1Supplier;

    protected Map<V, Map<T, Map<V, E>>> outEdges;
    protected Map<V, Map<T, Map<V, E>>> inEdges;
    protected Map<E, EdgeData> edges;

    public FastDirectedMultiGraph() {
        this(LinkedHashMap::new, v -> new LinkedHashMap<>(), v -> new LinkedHashMap<>());
    }

    public FastDirectedMultiGraph(
            Supplier<? extends Map<V, Map<T, Map<V, E>>>> mapSupplier,
            Function<? super V, ? extends Map<T, Map<V, E>>> map0Supplier,
            Function<? super T, ? extends Map<V, E>> map1Supplier) {
        this.mapSupplier = mapSupplier;
        this.map0Supplier = map0Supplier;
        this.map1Supplier = map1Supplier;
        outEdges = mapSupplier.get();
        inEdges = mapSupplier.get();
        edges = new LinkedHashMap<>();
    }

    protected void validate() {
        if(!Objects.equals(outEdges.keySet(), inEdges.keySet())) {
            throw new IllegalStateException("vertex mismatch");
        }

        Set<E> outE = outEdges.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.values().stream())
                .collect(CollectionUtil.collectToLinkedSet());
        Set<E> inE = inEdges.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .flatMap(m -> m.values().stream())
                .collect(CollectionUtil.collectToLinkedSet());
        if(!Objects.equals(outE, inE)) {
            throw new IllegalStateException("edges mismatch #1");
        }
        if(!Objects.equals(outE, edges.keySet())) {
            throw new IllegalStateException("edges mismatch #2");
        }
        if(!Objects.equals(edges.keySet(), inE)) {
            throw new IllegalStateException("edges mismatch #3");
        }
    }

    @Override
    public boolean addVertex(V vertex) {
        if(hasVertex(vertex)) {
            return false;
        } else {
            outEdges.put(vertex, map0Supplier.apply(vertex));
            inEdges.put(vertex, map0Supplier.apply(vertex));
            return true;
        }
    }

    @Override
    public boolean hasVertex(V vertex) {
        return outEdges.containsKey(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {
        if(hasVertex(vertex)) {
            outEdges.remove(vertex);
            inEdges.remove(vertex);
            for(Map<T, Map<V, E>> outMap0: outEdges.values()) {
                for(Map<V, E> outMap1: outMap0.values()) {
                    E edge = outMap1.remove(vertex);
                    edges.remove(edge);
                }
            }
            for(Map<T, Map<V, E>> inMap0: inEdges.values()) {
                for(Map<V, E> inMap1: inMap0.values()) {
                    E edge = inMap1.remove(vertex);
                    edges.remove(edge);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int vertexCount() {
        return outEdges.size();
    }

    @Override
    public int inDegree(V vertex, T type) {
        Map<T, Map<V, E>> map0 = inEdges.get(vertex);
        if(map0 == null) return 0;
        Map<V, E> map1 = map0.get(type);
        return map1 == null
                ? 0
                : map1.size();
    }

    @Override
    public int outDegree(V vertex, T type) {
        Map<T, Map<V, E>> map0 = outEdges.get(vertex);
        if(map0 == null) return 0;
        Map<V, E> map1 = map0.get(type);
        return map1 == null
                ? 0
                : map1.size();
    }

    @Override
    public int degree(V vertex, T type) {
        return inDegree(vertex, type) + outDegree(vertex, type);
    }

    @Override
    public Set<V> getVertices() {
        return outEdges.keySet();
    }

    @Override
    public Stream<V> streamVertices() {
        return outEdges.keySet().stream();
    }

    @Override
    public boolean hasEdge(E edge) {
        return edges.containsKey(edge);
    }

    @Override
    public boolean removeEdge(E edge) {
        EdgeData ed = edges.get(edge); //wird im naechsten Aufruf entfernt
        if(ed != null) {
            return removeEdge(ed.getSource(), ed.getTarget(), ed.getType());
        } else {
            return false;
        }
    }

    @Override
    public int edgeCount() {
        return edges.size();
    }

    @Override
    public Set<V> getTargets(V from, T type) {
        Map<T, Map<V, E>> map0 = outEdges.get(from);
        if(map0 == null) return Collections.emptySet();
        Map<V, E> map1 = map0.get(type);
        return map1 == null ? Collections.emptySet() : map1.keySet();
    }

    @Override
    public boolean getTargets(V from, T type, Collection<? super V> targets) {
        Set<V> t = getTargets(from, type);
        return targets.addAll(t);
    }

    @Override
    public Stream<V> streamTargets(V from, T type) {
        return getTargets(from, type).stream();
    }

    @Override
    public Set<V> getAllTargets(V vertex) {
        Map<T, Map<V, E>> map0 = outEdges.get(vertex);
        if(map0 == null) return Collections.emptySet();
        return map0.values()
                .stream()
                .flatMap(m -> m.keySet().stream())
                .collect(CollectionUtil.collectToLinkedSet());
    }

    @Override
    public boolean addEdge(V from, V to, T type, E edge) {
        if(hasEdge(edge) || hasEdge(from, to, type)) {
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

        Map<T, Map<V, E>> outMap0 = outEdges.get(from);
        Map<V, E> outMap1 = outMap0.computeIfAbsent(type, map1Supplier);
        E outE = outMap1.put(to, edge);

        Map<T, Map<V, E>> inMap0 = inEdges.get(to);
        Map<V, E> inMap1 = inMap0.computeIfAbsent(type, map1Supplier);
        E inE = inMap1.put(from, edge);

        edges.put(edge, new EdgeData(from, to, type));

        if(outE != inE) {
            throw new IllegalStateException();
        }
        return outE;
    }

    @Override
    public boolean hasEdge(V from, V to, T type) {
        Map<T, Map<V, E>> map0 = outEdges.get(from);
        if(map0 == null) return false;
        Map<V, E> map1 = map0.get(type);
        if(map1 == null) return false;
        return map1.containsKey(to);
    }

    @Override
    public boolean removeEdge(V from, V to, T type) {
        Map<T, Map<V, E>> outMap0 = outEdges.get(from);
        if(outMap0 == null) return false;
        Map<V, E> outMap1 = outMap0.get(type);
        if(outMap1 == null) return false;
        if(!outMap1.containsKey(to)) {
            return false;
        }
        E edge = outMap1.remove(to);

        Map<T, Map<V, E>> inMap0 = inEdges.get(to);
        Map<V, E> inMap1 = inMap0.get(type);
        inMap1.remove(from);

        edges.remove(edge);
        return true;
    }

    @Override
    public Set<E> removeAllEdges(T type) {
        Set<E> removed = new LinkedHashSet<>();
        for(Map.Entry<E, EdgeData> entry: edges.entrySet()) {
            if(entry.getValue().getType() == type) {
                removed.add(entry.getKey());
            }
        }
        for(E edge: removed) {
            removeEdge(edge);
        }
        return removed;
    }

    @Override
    public E getEdge(V from, V to, T type) {
        Map<T, Map<V, E>> map0 = outEdges.get(from);
        if(map0 == null) return null;
        Map<V, E> map1 = map0.get(type);
        if(map1 == null) return null;
        return map1.get(to);
    }

    @Override
    public Map<T, E> getEdges(V from, V to) {
        Map<T, E> edges = new LinkedHashMap<>();
        Map<T, Map<V, E>> map0 = outEdges.get(from);
        if(map0 == null) return edges;
        for(Map.Entry<T, Map<V, E>> entry: map0.entrySet()) {
            if(entry.getValue().containsKey(to)) {
                E edge = entry.getValue().get(to);
                edges.put(entry.getKey(), edge);
            }
        }
        return edges;
    }

    @Override
    public Collection<E> getAllEdges(T[] types) {
        List<E> list = new ArrayList<>();
        for(T type: types) {
            list.addAll(getEdges(type));
        }
        return list;
    }

    @Override
    public Set<E> getEdges(T type) {
        Set<E> e = new LinkedHashSet<>();
        for(Map.Entry<E, EdgeData> entry: edges.entrySet()) {
            if(entry.getValue().getType() == type) {
                e.add(entry.getKey());
            }
        }
        return e;
    }

    @Override
    public Stream<E> streamEdgesFrom(V from, T type) {
        Map<T, Map<V, E>> map0 = outEdges.get(from);
        if(map0 == null) return Stream.empty();
        Map<V, E> map1 = map0.get(type);
        return map1 == null
                ? Stream.empty()
                : map1.values().stream();
    }

    @Override
    public Stream<E> streamEdgesTo(V to, T type) {
        Map<T, Map<V, E>> map0 = inEdges.get(to);
        if(map0 == null) return Stream.empty();
        Map<V, E> map1 = map0.get(type);
        return map1 == null
                ? Stream.empty()
                : map1.values().stream();
    }

    /**
     * @author Daniel Abitz
     */
    protected class EdgeData {
        protected V source;
        protected V target;
        protected T type;

        protected EdgeData(V source, V target, T type) {
            this.source = source;
            this.target = target;
            this.type = type;
        }

        public V getSource() {
            return source;
        }

        public V getTarget() {
            return target;
        }

        public T getType() {
            return type;
        }
    }
}
