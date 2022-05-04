package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.util.Rnd;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This implementation stores incoming and outgoing edges for fast retrieval.
 *
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class CachedDirectedMultiGraph<V, E, T> implements DirectedMultiGraph<V, E, T> {

    protected Supplier<? extends Map<V, VertexData<V, E, T>>> verticiesSupplier;
    protected Supplier<? extends Map<E, EdgeData<V, E, T>>> edgesSupplier;
    protected Supplier<? extends Map<T, Map<V, E>>> vertexMapSupplier;
    protected Function<? super T, ? extends Map<V, E>> vertexMap0Supplier;

    protected Map<V, VertexData<V, E, T>> vertices;
    protected Map<E, EdgeData<V, E, T>> edges;

    protected boolean validate = false;

    public CachedDirectedMultiGraph() {
        this(
                LinkedHashMap::new,
                LinkedHashMap::new,
                LinkedHashMap::new,
                t -> new LinkedHashMap<>()
        );
    }

    public CachedDirectedMultiGraph(
            Supplier<? extends Map<V, VertexData<V, E, T>>> verticiesSupplier,
            Supplier<? extends Map<E, EdgeData<V, E, T>>> edgesSupplier,
            Supplier<? extends Map<T, Map<V, E>>> vertexMapSupplier,
            Function<? super T, ? extends Map<V, E>> vertexMap0Supplier) {
        this.verticiesSupplier = verticiesSupplier;
        this.edgesSupplier = edgesSupplier;
        this.vertexMapSupplier = vertexMapSupplier;
        this.vertexMap0Supplier = vertexMap0Supplier;
        this.vertices = verticiesSupplier.get();
        this.edges = edgesSupplier.get();
    }

    @Override
    public void print(Appendable target, Function<? super V, ? extends String> vToString, Function<? super E, ? extends String> eToString, Function<? super T, ? extends String> tToString) throws IOException {
        for(V vertex: getVertices()) {
            target.append(vToString.apply(vertex));
            target.append("\n");
            VertexData<V, E, T> vertexData = vertices.get(vertex);

            for(Map.Entry<T, Map<V, E>> inEdges: vertexData.getInEdges().entrySet()) {
                target.append("  in edges (");
                target.append(String.valueOf(tToString.apply(inEdges.getKey())));
                target.append(") (count=");
                target.append(Integer.toString(inEdges.getValue().size()));
                target.append(")\n");
                for(V linked: inEdges.getValue().keySet()) {
                    target.append("    ");
                    target.append(vToString.apply(linked));
                    target.append("\n");
                }
            }

            for(Map.Entry<T, Map<V, E>> outEdges: vertexData.getOutEdges().entrySet()) {
                target.append("  out edges (");
                target.append(String.valueOf(tToString.apply(outEdges.getKey())));
                target.append(") (count=");
                target.append(Integer.toString(outEdges.getValue().size()));
                target.append(")\n");
                for(V linked: outEdges.getValue().keySet()) {
                    target.append("    ");
                    target.append(vToString.apply(linked));
                    target.append("\n");
                }
            }
        }
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public boolean isValidate() {
        return validate;
    }

    protected void validate() {
        int outCount = 0;
        int inCount = 0;
        for(VertexData<V, E, T> vData: vertices.values()) {
            //from
            for(Map<V, E> outEdges: vData.getOutEdges().values()) {
                for(Map.Entry<V, E> outEdgeEntry: outEdges.entrySet()) {
                    EdgeData<V, E, T> outEdge = findEdge(outEdgeEntry.getValue());
                    VertexData<V, E, T> toData = findVertex(outEdgeEntry.getKey());
                    if(!toData.hasInEdge(outEdge)) {
                        throw new IllegalStateException("missing ingoing edge");
                    }
                    outCount++;
                }
            }
            //to
            for(Map<V, E> inEdges: vData.getInEdges().values()) {
                for(Map.Entry<V, E> inEdgeEntry: inEdges.entrySet()) {
                    EdgeData<V, E, T> inEdge = findEdge(inEdgeEntry.getValue());
                    VertexData<V, E, T> fromData = findVertex(inEdgeEntry.getKey());
                    if(!fromData.hasOutEdge(inEdge)) {
                        throw new IllegalStateException("missing outgoing edge");
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

    protected VertexData<V, E, T> toData(V vertex) {
        return new VertexData<>(vertexMapSupplier, vertexMap0Supplier, vertex);
    }

    protected EdgeData<V, E, T> toData(V from, V to, E edge, T type) {
        return new EdgeData<>(from, to, edge, type);
    }

    protected VertexData<V, E, T> findVertex(V vertex) {
        VertexData<V, E, T> data = vertices.get(vertex);
        if(data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    protected EdgeData<V, E, T> findEdge(E edge) throws NoSuchElementException {
        EdgeData<V, E, T> data = edges.get(edge);
        if(data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    @Override
    public boolean addVertex(V vertex) {
        if(hasVertex(vertex)) {
            return false;
        } else {
            VertexData<V, E, T> data = toData(vertex);
            vertices.put(vertex, data);
            return true;
        }
    }

    protected VertexData<V, E, T> addIfAbsent(V vertex) {
        if(hasVertex(vertex)) {
            return vertices.get(vertex);
        } else {
            VertexData<V, E, T> data = toData(vertex);
            vertices.put(vertex, data);
            return data;
        }
    }

    @Override
    public boolean hasVertex(V vertex) {
        return vertices.containsKey(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {
        VertexData<V, E, T> vData = vertices.remove(vertex);
        if(vData == null) {
            return false;
        }
        //remove ingoing edge from target
        for(Map<V, E> outMap: vData.getOutEdges().values()) {
            for(E outEdge: outMap.values()) {
                EdgeData<V, E, T> outEdgeData = findEdge(outEdge);
                VertexData<V, E, T> targetData = findVertex(outEdgeData.getTarget());
                targetData.removeInEdge(outEdgeData);
                edges.remove(outEdge);
            }
        }
        //remove outgoing edge from source
        for(Map<V, E> inMap: vData.getInEdges().values()) {
            for(E inEdge: inMap.values()) {
                EdgeData<V, E, T> inEdgeData = findEdge(inEdge);
                VertexData<V, E, T> sourceData = findVertex(inEdgeData.getSource());
                sourceData.removeOutEdge(inEdgeData);
                edges.remove(inEdge);
            }
        }
        return true;
    }

    @Override
    public int vertexCount() {
        return vertices.size();
    }

    @Override
    public Set<V> getVertices() {
        return vertices.keySet();
    }

    @Override
    public Stream<V> streamVertices() {
        return vertices.keySet().stream();
    }

    @Override
    public boolean hasEdge(E edge) {
        return edges.containsKey(edge);
    }

    @Override
    public boolean removeEdge(E edge) {
        EdgeData<V, E, T> edgeData = edges.remove(edge);
        if(edgeData == null) {
            return false;
        }
        removeEdgeFromVerticies(edgeData);
        return true;
    }

    protected void removeEdgeFromVerticies(EdgeData<V, E, T> edgeData) {
        //remove edge from source
        VertexData<V, E, T> fromData = vertices.get(edgeData.getSource());
        fromData.removeOutEdge(edgeData);
        //remove edge from target
        VertexData<V, E, T> toData = vertices.get(edgeData.getTarget());
        toData.removeInEdge(edgeData);
    }

    @Override
    public int edgeCount() {
        return edges.size();
    }

    @Override
    public int inDegree(V vertex, T type) {
        VertexData<V, E, T> data = vertices.get(vertex);
        if(data == null) return 0;
        return data.inDegree(type);
    }

    @Override
    public int outDegree(V vertex, T type) {
        VertexData<V, E, T> data = vertices.get(vertex);
        if(data == null) return 0;
        return data.outDegree(type);
    }

    @Override
    public int degree(V vertex, T type) {
        VertexData<V, E, T> data = vertices.get(vertex);
        if(data == null) return 0;
        return data.degree(type);
    }

    //=========================
    // targets
    //=========================

    @Override
    public Set<V> getTargets(V from, T type) {
        if(hasVertex(from)) {
            Set<V> targets = new LinkedHashSet<>();
            getTargets(from, type, targets);
            return targets;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean getTargets(V from, T type, Collection<? super V> targets) {
        VertexData<V, E, T> data = vertices.get(from);
        if(data == null) {
            return false;
        }
        return data.getTargets(type, targets);
    }

    @Override
    public Stream<V> streamTargets(V from, T type) {
        VertexData<V, E, T> data = vertices.get(from);
        if(data == null) {
            return Stream.empty();
        }
        return data.streamTargets(type);
    }

    @Override
    public Set<V> getAllTargets(V from) {
        if(hasVertex(from)) {
            Set<V> targets = new LinkedHashSet<>();
            getAllTargets(from, targets);
            return targets;
        } else {
            return Collections.emptySet();
        }
    }

    public boolean getAllTargets(V from, Collection<? super V> targets) {
        VertexData<V, E, T> data = vertices.get(from);
        if(data == null) {
            return false;
        }
        return data.getAllTargets(targets);
    }

    @Override
    public V getRandomTarget(V from, T type, Rnd rnd) {
        VertexData<V, E, T> data = vertices.get(from);
        if(data == null) {
            return null;
        }
        return data.getRandomTarget(type, rnd);
    }

    //=========================
    // sources
    //=========================

    @Override
    public Set<V> getSources(V to, T type) {
        if(hasVertex(to)) {
            Set<V> targets = new LinkedHashSet<>();
            getSources(to, type, targets);
            return targets;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean getSources(V to, T type, Collection<? super V> sources) {
        VertexData<V, E, T> data = vertices.get(to);
        if(data == null) {
            return false;
        }
        return data.getSources(type, sources);
    }

    @Override
    public Stream<V> streamSources(V to, T type) {
        VertexData<V, E, T> data = vertices.get(to);
        if(data == null) {
            return Stream.empty();
        }
        return data.streamSources(type);
    }

    @Override
    public Set<V> getAllSources(V to) {
        if(hasVertex(to)) {
            Set<V> sources = new LinkedHashSet<>();
            getAllSources(to, sources);
            return sources;
        } else {
            return Collections.emptySet();
        }
    }

    public boolean getAllSources(V from, Collection<? super V> sources) {
        VertexData<V, E, T> data = vertices.get(from);
        if(data == null) {
            return false;
        }
        return data.getAllSources(sources);
    }

    //=========================
    // sources and targets
    //=========================

    @Override
    public Set<V> getSourcesAndTargets(V fromOrTo, T type) {
        if(hasVertex(fromOrTo)) {
            Set<V> targets = new LinkedHashSet<>();
            getSourcesAndTargets(fromOrTo, type, targets);
            return targets;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean getSourcesAndTargets(V fromOrTo, T type, Collection<? super V> sourcesAndTargets) {
        VertexData<V, E, T> data = vertices.get(fromOrTo);
        if(data == null) {
            return false;
        }
        return data.getSourcesAndTargets(type, sourcesAndTargets);
    }

    @Override
    public Stream<V> streamSourcesAndTargets(V fromOrTo, T type) {
        VertexData<V, E, T> data = vertices.get(fromOrTo);
        if(data == null) {
            return Stream.empty();
        }
        return data.streamSourcesAndTargets(type);
    }

    @Override
    public Set<V> getAllSourcesAndTargets(V fromOrTo) {
        if(hasVertex(fromOrTo)) {
            Set<V> sourcesAndTargets = new LinkedHashSet<>();
            getAllSourcesAndTargets(fromOrTo, sourcesAndTargets);
            return sourcesAndTargets;
        } else {
            return Collections.emptySet();
        }
    }

    public boolean getAllSourcesAndTargets(V fromOrTo, Collection<? super V> sourcesAndTargets) {
        VertexData<V, E, T> data = vertices.get(fromOrTo);
        if(data == null) {
            return false;
        }
        return data.getAllSourcesAndTargets(sourcesAndTargets);
    }

    //=========================
    // edges
    //=========================

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
        VertexData<V, E, T> fromData = addIfAbsent(from);
        VertexData<V, E, T> toData = addIfAbsent(to);
        EdgeData<V, E, T> newEdgeData = toData(from, to, edge, type);

        E oldEdge = removeAndGetEdge(from, to, type);
        edges.put(edge, newEdgeData);
        fromData.setOutEdge(newEdgeData);
        toData.setInEdge(newEdgeData);
        return oldEdge;
    }

    @Override
    public boolean hasEdge(V from, V to, T type) {
        VertexData<V, E, T> fromData = vertices.get(from);
        if(fromData == null) return false;
        return fromData.hasOutEdge(to, type);
    }

    @Override
    public boolean removeEdge(V from, V to, T type) {
        E removed = removeAndGetEdge(from, to, type);
        return removed != null;
    }

    protected E removeAndGetEdge(V from, V to, T type) {
        E edge = getEdge(from, to, type);
        if(edge == null) return null;
        removeEdge(edge);
        return edge;
    }

    @Override
    public Set<E> removeAllEdges(T type) {
        Set<E> removed = new LinkedHashSet<>();
        removeAllEdges(type, removed);
        return removed;
    }

    public boolean removeAllEdges(T type, Collection<? super E> outEdges) {
        boolean changed = false;
        Iterator<Map.Entry<E, EdgeData<V, E, T>>> iter = edges.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<E, EdgeData<V, E, T>> next = iter.next();
            EdgeData<V, E, T> nextData = next.getValue();
            if(nextData.getType() == type) {
                iter.remove();
                removeEdgeFromVerticies(nextData);
                outEdges.add(nextData.getEdge());
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public E getEdge(V from, V to, T type) {
        VertexData<V, E, T> fromData = vertices.get(from);
        if(fromData == null) return null;
        return fromData.getOutEdge(to, type);
    }

    @Override
    public Map<T, E> getEdges(V from, V to) {
        VertexData<V, E, T> fromData = vertices.get(from);
        if(fromData == null) {
            return Collections.emptyMap();
        }
        Map<T, E> edges = new LinkedHashMap<>();
        fromData.getOutEdges(to, edges);
        return edges;
    }

    @Override
    public Collection<E> getAllEdges() {
        return getAllEdges(null);
    }

    @Override
    public Collection<E> getAllEdges(T[] types) {
        List<E> edges = new ArrayList<>();
        getAllEdges(types, edges);
        return edges;
    }

    @Override
    public Stream<E> streamAllEdges() {
        return streamAllEdges(null);
    }

    @Override
    public Stream<E> streamAllEdges(T[] types) {
        return edges.values()
                .stream()
                .filter(edgeData -> has(edgeData.getType(), types))
                .map(EdgeData::getEdge);
    }

    protected static <T> boolean has(T type, T[] types) {
        if(types == null) {
            return true;
        }
        for(T t: types) {
            if(t == type) {
                return true;
            }
        }
        return false;
    }

    protected boolean getAllEdges(T[] types, Collection<? super E> outEdges) {
        boolean changed = false;
        for(EdgeData<V, E, T> edgeData: edges.values()) {
            if(has(edgeData.getType(), types)) {
                changed |= outEdges.add(edgeData.getEdge());
            }
        }
        return changed;
    }

    @Override
    public Set<E> getEdges(T type) {
        Set<E> edges = new LinkedHashSet<>();
        getEdges(type, edges);
        return edges;
    }

    protected boolean getEdges(T type, Collection<? super E> outEdges) {
        boolean changed = false;
        for(EdgeData<V, E, T> edgeData: edges.values()) {
            if(edgeData.getType() == type) {
                changed |= outEdges.add(edgeData.getEdge());
            }
        }
        return changed;
    }

    @Override
    public Stream<E> streamEdgesFrom(V from, T type) {
        VertexData<V, E, T> fromData = vertices.get(from);
        if(fromData == null) {
            return Stream.empty();
        }
        return fromData.streamEdgesFromThis(type);
    }

    @Override
    public Stream<E> streamEdgesTo(V to, T type) {
        VertexData<V, E, T> toData = vertices.get(to);
        if(toData == null) {
            return Stream.empty();
        }
        return toData.streamEdgesToThis(type);
    }

    //=========================
    // helper
    //=========================

    /**
     * @param <V>
     * @param <E>
     * @param <T>
     * @author Daniel Abitz
     */
    public static class VertexData<V, E, T> {

        protected Supplier<? extends Map<T, Map<V, E>>> mapSupplier;
        protected Function<? super T, ? extends Map<V, E>> map0Supplier;

        protected V vertex;
        protected Map<T, Map<V, E>> outEdges; //== targets
        protected Map<T, Map<V, E>> inEdges; //== sources

        protected VertexData(
                Supplier<? extends Map<T, Map<V, E>>> mapSupplier,
                Function<? super T, ? extends Map<V, E>> map0Supplier,
                V vertex) {
            this.mapSupplier = mapSupplier;
            this.map0Supplier = map0Supplier;
            this.vertex = vertex;
            outEdges = mapSupplier.get();
            inEdges = mapSupplier.get();
        }

        protected V getVertex() {
            return vertex;
        }

        protected boolean hasOutEdge(EdgeData<V, E, T> data) {
            return hasOutEdge(data.getTarget(), data.getType());
        }

        protected boolean hasOutEdge(V to, T type) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return false;
            return toMap.containsKey(to);
        }

        protected boolean hasInEdge(EdgeData<V, E, T> data) {
            return hasInEdge(data.getSource(), data.getType());
        }

        protected boolean hasInEdge(V from, T type) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return false;
            return fromMap.containsKey(from);
        }

        protected boolean addOutEdge(EdgeData<V, E, T> data) {
            return addOutEdge(data.getTarget(), data.getType(), data.getEdge());
        }

        protected boolean addOutEdge(V to, T type, E edge) {
            if(hasOutEdge(to, type)) {
                return false;
            } else {
                E old = setOutEdge(to, type, edge);
                if(old != null) {
                    throw new IllegalStateException("edge already exists");
                }
                return true;
            }
        }

        protected boolean addInEdge(EdgeData<V, E, T> data) {
            return addInEdge(data.getSource(), data.getType(), data.getEdge());
        }

        protected boolean addInEdge(V from, T type, E edge) {
            if(hasInEdge(from, type)) {
                return false;
            } else {
                E old = setInEdge(from, type, edge);
                if(old != null) {
                    throw new IllegalStateException("edge already exists");
                }
                return true;
            }
        }

        protected E setOutEdge(EdgeData<V, E, T> data) {
            return setOutEdge(data.getTarget(), data.getType(), data.getEdge());
        }

        protected E setOutEdge(V to, T type, E edge) {
            Map<V, E> toMap = outEdges.computeIfAbsent(type, map0Supplier);
            return toMap.put(to, edge);
        }

        protected E setInEdge(EdgeData<V, E, T> data) {
            return setInEdge(data.getSource(), data.getType(), data.getEdge());
        }

        protected E setInEdge(V from, T type, E edge) {
            Map<V, E> fromMap = inEdges.computeIfAbsent(type, map0Supplier);
            return fromMap.put(from, edge);
        }

        protected E removeOutEdge(EdgeData<V, E, T> data) {
            return removeOutEdge(data.getTarget(), data.getType());
        }

        protected E removeOutEdge(V to, T type) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return null;
            return toMap.remove(to);
        }

        protected E removeInEdge(EdgeData<V, E, T> data) {
            return removeInEdge(data.getSource(), data.getType());
        }

        protected E removeInEdge(V from, T type) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return null;
            return fromMap.remove(from);
        }

        protected E getOutEdge(EdgeData<V, E, T> data) {
            return getOutEdge(data.getTarget(), data.getType());
        }

        protected E getOutEdge(V to, T type) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return null;
            return toMap.get(to);
        }

        protected E getInEdge(EdgeData<V, E, T> data) {
            return getInEdge(data.getSource(), data.getType());
        }

        protected E getInEdge(V from, T type) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return null;
            return fromMap.get(from);
        }

        protected boolean getTargets(T type, Collection<? super V> targets) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return false;
            return targets.addAll(toMap.keySet());
        }

        protected Stream<V> streamTargets(T type) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return Stream.empty();
            return toMap.keySet().stream();
        }

        protected boolean getSources(T type, Collection<? super V> sources) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return false;
            return sources.addAll(fromMap.keySet());
        }

        protected Stream<V> streamSources(T type) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return Stream.empty();
            return fromMap.keySet().stream();
        }

        @SuppressWarnings("ConstantConditions")
        protected boolean getSourcesAndTargets(T type, Collection<? super V> sourcesAndTargets) {
            boolean changed = false;
            changed |= getSources(type, sourcesAndTargets);
            changed |= getTargets(type, sourcesAndTargets);
            return changed;
        }

        protected Stream<V> streamSourcesAndTargets(T type) {
            return Stream.concat(streamTargets(type), streamSources(type));
        }

        protected Stream<E> streamEdgesFromThis(T type) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return Stream.empty();
            return toMap.values().stream();
        }

        protected Stream<E> streamEdgesToThis(T type) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return Stream.empty();
            return fromMap.values().stream();
        }

        protected boolean getAllTargets(Collection<? super V> targets) {
            boolean changed = false;
            for(Map<V, E> toMap: outEdges.values()) {
                changed |= targets.addAll(toMap.keySet());
            }
            return changed;
        }

        protected boolean getAllSources(Collection<? super V> sources) {
            boolean changed = false;
            for(Map<V, E> fromMap: inEdges.values()) {
                changed |= sources.addAll(fromMap.keySet());
            }
            return changed;
        }

        @SuppressWarnings("ConstantConditions")
        protected boolean getAllSourcesAndTargets(Collection<? super V> sourcesAndTargets) {
            boolean changed = false;
            changed |= getAllSources(sourcesAndTargets);
            changed |= getAllTargets(sourcesAndTargets);
            return changed;
        }

        protected Map<T, Map<V, E>> getOutEdges() {
            return outEdges;
        }

        protected Map<T, Map<V, E>> getInEdges() {
            return inEdges;
        }

        protected V getRandomTarget(T type, Rnd rnd) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return null;
            return rnd.getRandom(toMap.keySet());
        }

        protected V getRandomSource(T type, Rnd rnd) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return null;
            return rnd.getRandom(fromMap.keySet());
        }

        protected E getRandomOutEdge(T type, Rnd rnd) {
            Map<V, E> toMap = outEdges.get(type);
            if(toMap == null) return null;
            return rnd.getRandom(toMap.values());
        }

        protected E getRandomInEdge(T type, Rnd rnd) {
            Map<V, E> fromMap = inEdges.get(type);
            if(fromMap == null) return null;
            return rnd.getRandom(fromMap.values());
        }

        protected boolean getOutEdges(V to, Map<? super T, ? super E> edges) {
            boolean changed = false;
            for(Map.Entry<T, Map<V, E>> outEntry: outEdges.entrySet()) {
                E edge = outEntry.getValue().get(to);
                if(edge != null) {
                    edges.put(outEntry.getKey(), edge);
                    changed = true;
                }
            }
            return changed;
        }

        protected boolean getInEdges(V from, Map<? super T, ? super E> edges) {
            boolean changed = false;
            for(Map.Entry<T, Map<V, E>> InEntry: inEdges.entrySet()) {
                E edge = InEntry.getValue().get(from);
                if(edge != null) {
                    edges.put(InEntry.getKey(), edge);
                    changed = true;
                }
            }
            return changed;
        }

        protected int degree(T type) {
            return inDegree(type) + outDegree(type);
        }

        protected int outDegree(T type) {
            Map<V, E> toMap = outEdges.get(type);
            return toMap == null
                    ? 0
                    : toMap.size();
        }

        protected int inDegree(T type) {
            Map<V, E> fromMap = inEdges.get(type);
            return fromMap == null
                    ? 0
                    : fromMap.size();
        }
    }

    /**
     * @param <V>
     * @param <E>
     * @param <T>
     * @author Daniel Abitz
     */
    public static class EdgeData<V, E, T> {

        protected V source;
        protected V target;
        protected E edge;
        protected T type;

        protected EdgeData(V source, V target, E edge, T type) {
            this.source = source;
            this.target = target;
            this.edge = edge;
            this.type = type;
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
    }
}
