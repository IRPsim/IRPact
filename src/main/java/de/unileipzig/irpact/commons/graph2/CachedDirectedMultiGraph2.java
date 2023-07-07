package de.unileipzig.irpact.commons.graph2;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.map.Map3;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class CachedDirectedMultiGraph2<V, E, T> implements DirectedMultiGraph2<V, E, T> {

    protected Supplier<? extends Map<V, VertexData>> verticiesSupplier;
    protected Supplier<? extends Map<E, EdgeData>> edgesSupplier;
    protected Supplier<? extends Map3<T, V, E>> vertexEdgesSupplier;
    protected Supplier<? extends Map<T, Set<V>>> lazyNeighboursMapSupplier;
    protected Supplier<? extends Set<V>> lazyNeighboursSetSupplier;

    protected Map<V, VertexData> vertices;
    protected Map<E, EdgeData> edges;

    protected boolean cacheNeighbours = false;

    public CachedDirectedMultiGraph2(
            Supplier<? extends Map<V, VertexData>> verticiesSupplier,
            Supplier<? extends Map<E, EdgeData>> edgesSupplier,
            Supplier<? extends Map3<T, V, E>> vertexEdgesSupplier,
            Supplier<? extends Map<T, Set<V>>> lazyNeighboursMapSupplier,
            Supplier<? extends Set<V>> lazyNeighboursSetSupplier) {
        this.verticiesSupplier = verticiesSupplier;
        this.edgesSupplier = edgesSupplier;
        this.vertexEdgesSupplier = vertexEdgesSupplier;
        this.lazyNeighboursMapSupplier = lazyNeighboursMapSupplier;
        this.lazyNeighboursSetSupplier = lazyNeighboursSetSupplier;

        vertices = verticiesSupplier.get();
        edges = edgesSupplier.get();
    }

    protected final Function<T, Set<V>> lazyNeighboursSetFunction = type -> getLazyNeighboursSetSupplier().get();
    protected Supplier<? extends Set<V>> getLazyNeighboursSetSupplier() {
        return lazyNeighboursSetSupplier;
    }

    //=========================
    //util
    //=========================

    public void setCacheNeighbours(boolean cacheNeighbours) {
        this.cacheNeighbours = cacheNeighbours;
    }

    public boolean isCacheNeighbours() {
        return cacheNeighbours;
    }

    public void validate() throws NoSuchElementException, IllegalStateException {
        int outCount = 0;
        int inCount = 0;
        for(VertexData vData: vertices.values()) {
            //from
            for(Map<V, E> outEdges: vData.outEdges.getMap().values()) {
                for(Map.Entry<V, E> outEdgeEntry: outEdges.entrySet()) {
                    VertexData toData = findVertex(outEdgeEntry.getKey());
                    EdgeData outEdge = findEdge(outEdgeEntry.getValue());
                    if(!toData.hasInEdge(outEdge)) {
                        throw new IllegalStateException("missing ingoing edge");
                    }
                    outCount++;
                }
            }
            //to
            for(Map<V, E> inEdges: vData.inEdges.getMap().values()) {
                for(Map.Entry<V, E> inEdgeEntry: inEdges.entrySet()) {
                    VertexData fromData = findVertex(inEdgeEntry.getKey());
                    EdgeData inEdge = findEdge(inEdgeEntry.getValue());
                    if(!fromData.hasOutEdge(inEdge)) {
                        throw new IllegalStateException("missing ingoing edge");
                    }
                    inCount++;
                }
            }
        }
        //edges
        if(edges.size() != outCount) {
            throw new IllegalStateException("outgoing edge count mismatch: " + edges.size() + " != " + outCount);
        }
        if(edges.size() != inCount) {
            throw new IllegalStateException("ingoing edge count mismatch: " + edges.size() + " != " + inCount);
        }
    }

    public VertexData findVertex(V vertex) throws NoSuchElementException {
        VertexData data = vertices.get(vertex);
        if(data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public EdgeData findEdge(E edge) throws NoSuchElementException {
        EdgeData data = edges.get(edge);
        if(data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    //=========================
    //vertex
    //=========================

    @Override
    public boolean addVertex(V vertex) {
        if(hasVertex(vertex)) {
            return false;
        } else {
            VertexData data = new VertexData(vertex);
            vertices.put(vertex, data);
            return true;
        }
    }

    protected VertexData addIfAbsent(V vertex) {
        VertexData data = vertices.get(vertex);
        if(data == null) {
            data = new VertexData(vertex);
            vertices.put(vertex, data);
        }
        return data;
    }

    @Override
    public boolean hasVertex(V vertex) {
        return vertices.containsKey(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {
        VertexData data = vertices.remove(vertex);
        if(data == null) {
            return false;
        } else {
            data.removeVertex();
            return true;
        }
    }

    @Override
    public Stream<V> streamVertices() {
        return vertices.keySet().stream();
    }

    //=========================
    //edges
    //=========================

    @Override
    public boolean addEdge(T type, V from, V to, E edge) {
        if(hasEdge(edge) || hasEdge(type, from, to)) {
            return false;
        } else {
            setEdge(type, from, to, edge);
            return true;
        }
    }

    @Override
    public E setEdge(T type, V from, V to, E edge) {
        VertexData fromData = addIfAbsent(from);
        VertexData toData = addIfAbsent(to);
        EdgeData edgeData = new EdgeData(type, from, to, edge);

        E oldEdge = removeAndGetEdge(type, from, to);
        edges.put(edge, edgeData);
        fromData.setOutEdge(edgeData);
        toData.setInEdge(edgeData);
        return oldEdge;
    }

    @Override
    public boolean hasEdge(E edge) {
        return edges.containsKey(edge);
    }

    @Override
    public boolean hasEdge(T type, V from, V to) {
        return getEdge(type, from, to) != null;
    }

    @Override
    public boolean removeEdge(T type, V from, V to) {
        return removeAndGetEdge(type, from, to) != null;
    }

    @Override
    public boolean removeEdge(E edge) {
        EdgeData data = edges.get(edge);
        if(data == null) {
            return false;
        } else {
            data.removeEdge();
            return true;
        }
    }

    protected E removeAndGetEdge(T type, V from, V to) {
        E edge = getEdge(type, from, to);
        if(edge == null) {
            return null;
        } else {
            removeEdge(edge);
            return edge;
        }
    }

    @Override
    public E getEdge(T type, V from, V to) {
        VertexData data = vertices.get(from);
        return data == null
                ? null
                : data.getOutEdge(type, to);
    }

    @Override
    public Stream<E> streamEdges() {
        return edges.keySet().stream();
    }

    @Override
    public Stream<E> streamEdges(T type) {
        return edges.values()
                .stream()
                .filter(e -> e.getType() == type)
                .map(EdgeData::getEdge);
    }

    @Override
    public Set<E> getEdges(T type) {
        return streamEdges(type).collect(Collectors.toSet());
    }

    @Override
    public void removeAllEdges(T type) {
        List<EdgeData> edgesToRemove = edges.values()
                .stream()
                .filter(e -> e.hasType(type))
                .collect(Collectors.toList());
        for(EdgeData toRemove: edgesToRemove) {
            toRemove.removeEdge();
        }
    }

    //=========================
    // targets
    //=========================

    @Override
    public Stream<V> streamTargets(T type, V from) {
        VertexData data = vertices.get(from);
        return data == null
                ? Stream.empty()
                : data.streamTargets(type);
    }

    @Override
    public V getRandomTarget(T type, V from, Rnd rnd) {
        VertexData data = vertices.get(from);
        return data == null
                ? null
                : data.getRandomTarget(type, rnd);
    }

    //=========================
    // sources
    //=========================

    @Override
    public Stream<V> streamSources(T type, V to) {
        VertexData data = vertices.get(to);
        return data == null
                ? Stream.empty()
                : data.streamSources(type);
    }

    @Override
    public V getRandomSource(T type, V to, Rnd rnd) {
        VertexData data = vertices.get(to);
        return data == null
                ? null
                : data.getRandomSource(type, rnd);
    }

    //=========================
    // sources and targets
    //=========================

    @Override
    public Stream<V> streamNeighbours(T type, V vertex) {
        VertexData data = vertices.get(vertex);
        return data == null
                ? Stream.empty()
                : data.streamNeighbours(type);
    }

    //==================================================
    // helper
    //==================================================

    /**
     * @author Daniel Abitz
     */
    protected class VertexData {

        protected Map3<T, V, E> outEdges = vertexEdgesSupplier.get();
        protected Map3<T, V, E> inEdges = vertexEdgesSupplier.get();
        protected Map<T, Set<V>> lazyNeighbours;
        protected V vertex;

        protected VertexData(V vertex) {
            this.vertex = vertex;
        }

        //=========================
        //util
        //=========================

        protected void removeVertex() {
            //remove ingoing edge from target
            for(Map<V, E> targets: outEdges.values()) {
                for(E outEdge: targets.values()) {
                    EdgeData edgeData = findEdge(outEdge);
                    VertexData targetData = findVertex(edgeData.getTarget());
                    targetData.removeInEdge(edgeData);
                    edges.remove(outEdge);
                }
            }
            //remove outgoing edge from source
            for(Map<V, E> sources: inEdges.values()) {
                for(E inEdge: sources.values()) {
                    EdgeData edgeData = findEdge(inEdge);
                    VertexData sourceData = findVertex(edgeData.getTarget());
                    sourceData.removeOutEdge(edgeData);
                    edges.remove(inEdge);
                }
            }
        }

        //=========================
        //in
        //=========================

        protected boolean hasInEdge(EdgeData data) {
            return hasInEdge(data.getType(), data.getSource());
        }

        protected boolean hasInEdge(T type, V from) {
            return inEdges.contains(type, from);
        }

        protected boolean addInEdge(EdgeData data) {
            return addInEdge(data.getType(), data.getSource(), data.getEdge());
        }

        protected boolean addInEdge(T type, V from, E edge) {
            if(hasInEdge(type, from)) {
                return false;
            } else {
                E old = setInEdge(type, from, edge);
                if(old != null) {
                    throw new IllegalStateException("edge already exists");
                }
                addNeighbour(type, from);
                return true;
            }
        }

        protected E setInEdge(EdgeData data) {
            return setInEdge(data.getType(), data.getSource(), data.getEdge());
        }

        protected E setInEdge(T type, V from, E edge) {
            return inEdges.put(type, from, edge);
        }

        protected E removeInEdge(EdgeData data) {
            return removeInEdge(data.getType(), data.getSource());
        }

        protected E removeInEdge(T type, V from) {
            E edge = inEdges.remove(type, from);
            if(edge != null) {
                removeNeighbour(type, from);
            }
            return edge;
        }

        protected Stream<V> streamSources(T type) {
            return inEdges.streamKeys(type);
        }

        protected V getRandomSource(T type, Rnd rnd) {
            Map<V, E> sources = inEdges.get(type);
            return sources == null || sources.isEmpty()
                    ? null
                    : rnd.getRandomKey(sources);
        }

        //=========================
        //out
        //=========================

        protected boolean hasOutEdge(EdgeData data) {
            return hasOutEdge(data.getType(), data.getTarget());
        }

        protected boolean hasOutEdge(T type, V to) {
            return outEdges.contains(type, to);
        }

        public E getOutEdge(T type, V to) {
            return outEdges.get(type, to);
        }

        protected boolean addOutEdge(EdgeData data) {
            return addOutEdge(data.getType(), data.getTarget(), data.getEdge());
        }

        protected boolean addOutEdge(T type, V to, E edge) {
            if(hasOutEdge(type, to)) {
                return false;
            } else {
                E old = setOutEdge(type, to, edge);
                if(old != null) {
                    throw new IllegalStateException("edge already exists");
                }
                addNeighbour(type, to);
                return true;
            }
        }

        protected E setOutEdge(EdgeData data) {
            return setOutEdge(data.getType(), data.getTarget(), data.getEdge());
        }

        protected E setOutEdge(T type, V to, E edge) {
            return outEdges.put(type, to, edge);
        }

        protected E removeOutEdge(EdgeData data) {
            return removeOutEdge(data.getType(), data.getTarget());
        }

        protected E removeOutEdge(T type, V to) {
            E edge = outEdges.remove(type, to);
            if(edge != null) {
                removeNeighbour(type, to);
            }
            return edge;
        }

        protected Stream<V> streamTargets(T type) {
            return outEdges.streamKeys(type);
        }

        protected V getRandomTarget(T type, Rnd rnd) {
            Map<V, E> targets = outEdges.get(type);
            return targets == null || targets.isEmpty()
                    ? null
                    : rnd.getRandomKey(targets);
        }

        //=========================
        //neighbours
        //=========================

        protected boolean hasNeighbour(T type, V neighbour) {
            return hasInEdge(type, neighbour) || hasOutEdge(type, neighbour);
        }

        protected void addNeighbour(T type, V neighbour) {
            Map<T, Set<V>> lazyNeighbours = this.lazyNeighbours;
            if(lazyNeighbours != null) {
                lazyNeighbours.computeIfAbsent(type, lazyNeighboursSetFunction)
                        .add(neighbour);
            }
        }

        protected void removeNeighbour(T type, V neighbour) {
            Map<T, Set<V>> lazyNeighbours = this.lazyNeighbours;
            if(lazyNeighbours != null && !hasNeighbour(type, neighbour)) {
                Set<V> neighbours = lazyNeighbours.get(type);
                if(neighbours != null) {
                    neighbours.remove(neighbour);
                }
            }
        }

        protected Stream<V> streamNeighbours(T type) {
            if(cacheNeighbours) {
                Map<T, Set<V>> neighbours = getNeighbours();
                Set<V> vertices = neighbours.get(type);
                return vertices == null
                        ? Stream.empty()
                        : vertices.stream();
            } else {
                return Stream.concat(streamSources(type), streamTargets(type));
            }
        }

        protected Map<T, Set<V>> getNeighbours() {
            if(lazyNeighbours == null) {
                Map<T, Set<V>> n = lazyNeighboursMapSupplier.get();
                putAllNeighbours(n, outEdges);
                putAllNeighbours(n, inEdges);
                lazyNeighbours = n;
            }
            return lazyNeighbours;
        }

        protected void putAllNeighbours(Map<T, Set<V>> neighbours, Map3<T, V, E> edges) {
            for(Map.Entry<T, Map<V, E>> entry0: edges.getMap().entrySet()) {
                for(Map.Entry<V, E> entry1: entry0.getValue().entrySet()) {
                    neighbours.computeIfAbsent(entry0.getKey(), lazyNeighboursSetFunction)
                            .add(entry1.getKey());
                }
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected class EdgeData {

        protected T type;
        protected V source;
        protected V target;
        protected E edge;

        protected EdgeData(T type, V source, V target, E edge) {
            this.type = type;
            this.source = source;
            this.target = target;
            this.edge = edge;
        }

        protected V getSource() {
            return source;
        }

        protected V getTarget() {
            return target;
        }

        protected E getEdge() {
            return edge;
        }

        protected T getType() {
            return type;
        }

        protected boolean hasType(T type) {
            return Objects.equals(this.type, type);
        }

        protected void removeEdge() {
            //remove edge from source
            vertices.get(source).removeOutEdge(this);
            //remove edge from target
            vertices.get(target).removeInEdge(this);
        }

        @Override
        public String toString() {
            return "EdgeData{" +
                    "type=" + type +
                    ", source=" + source +
                    ", target=" + target +
                    ", edge=" + edge +
                    '}';
        }
    }
}
