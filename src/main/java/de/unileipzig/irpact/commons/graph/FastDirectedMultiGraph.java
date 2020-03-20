package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class FastDirectedMultiGraph<N extends Node, E extends Edge<N>, T> implements MultiGraph<N, E, T> {

    protected Supplier<? extends Set<E>> edgeSetSupplier;
    protected Supplier<? extends Map<T, Set<E>>> typeEdgeMapSupplier;
    protected Supplier<? extends Map<N, Map<T, E>>> nodeEdgeMapSupplier;
    protected Supplier<? extends Map<T, E>> edgeMapSupplier;
    protected Set<N> nodes;
    protected Map<T, Set<E>> edges;
    protected Map<N, Map<T, Set<E>>> outEdges;
    protected Map<N, Map<T, Set<E>>> inEdges;
    protected Map<N, Map<N, Map<T, E>>> nodeEdgeMapping;

    public FastDirectedMultiGraph(
            Set<N> nodes,
            Map<T, Set<E>> edges,
            Map<N, Map<T, Set<E>>> outEdges,
            Map<N, Map<T, Set<E>>> inEdges,
            Map<N, Map<N, Map<T, E>>> nodeEdgeMapping) {
        this(HashSet::new, HashMap::new, HashMap::new, HashMap::new, nodes, edges, outEdges, inEdges, nodeEdgeMapping);
    }

    public FastDirectedMultiGraph(
            Supplier<? extends Set<E>> edgeSetSupplier,
            Supplier<? extends Map<T, Set<E>>> typeEdgeMapSupplier,
            Supplier<? extends Map<N, Map<T, E>>> nodeEdgeMapSupplier,
            Supplier<? extends Map<T, E>> edgeMapSupplier,
            Set<N> nodes,
            Map<T, Set<E>> edges,
            Map<N, Map<T, Set<E>>> outEdges,
            Map<N, Map<T, Set<E>>> inEdges,
            Map<N, Map<N, Map<T, E>>> nodeEdgeMapping) {
        this.edgeSetSupplier = edgeSetSupplier;
        this.typeEdgeMapSupplier = typeEdgeMapSupplier;
        this.nodeEdgeMapSupplier = nodeEdgeMapSupplier;
        this.edgeMapSupplier = edgeMapSupplier;
        this.nodes = nodes;
        this.edges = edges;
        this.outEdges = outEdges;
        this.inEdges = inEdges;
        this.nodeEdgeMapping = nodeEdgeMapping;
    }

    protected boolean scanNode(N node) {
        if(nodes.contains(node)) return true;
        if(outEdges.containsKey(node)) return true;
        if(inEdges.containsKey(node)) return true;
        if(nodeEdgeMapping.containsKey(node)) return true;
        for(Map<N, Map<T, E>> map: nodeEdgeMapping.values()) {
            if(map.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge) {
        for(Set<E> set: edges.values()) {
            if(set.contains(edge)) {
                return true;
            }
        }
        for(Map<T, Set<E>> map: outEdges.values()) {
            for(Set<E> set: map.values()) {
                if(set.contains(edge)) {
                    return true;
                }
            }
        }
        for(Map<T, Set<E>> map: inEdges.values()) {
            for(Set<E> set: map.values()) {
                if(set.contains(edge)) {
                    return true;
                }
            }
        }
        for(Map<N, Map<T, E>> map: nodeEdgeMapping.values()) {
            for(Map<T, E> map1: map.values()) {
                if(map1.containsValue(edge)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge, T type) {
        Set<E> set = edges.get(type);
        if(set != null && set.contains(edge)) {
            return true;
        }
        for(Map<T, Set<E>> map: outEdges.values()) {
            Set<E> set1 = map.get(type);
            if(set1 != null && set1.contains(edge)) {
                return true;
            }
        }
        for(Map<T, Set<E>> map: inEdges.values()) {
            Set<E> set1 = map.get(type);
            if(set1 != null && set1.contains(edge)) {
                return true;
            }
        }
        for(Map<N, Map<T, E>> map: nodeEdgeMapping.values()) {
            for(Map<T, E> map1: map.values()) {
                E e = map1.get(type);
                return e == edge;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends N> getNodes() {
        return nodes;
    }

    @Override
    public Collection<? extends E> getEdges(T type) {
        return edges.get(type);
    }

    @Override
    public int getDegree(N node, T type) {
        int in = getIndegree(node, type);
        int out = getOutdegree(node, type);
        if(in == -1 && out == -1) {
            return -1;
        }
        return (in == -1 ? 0 : in)
                + (out == -1 ? 0 : out);
    }

    @Override
    public int getIndegree(N node, T type) {
        Map<T, Set<E>> map = inEdges.get(node);
        if(map == null) {
            return -1;
        }
        Set<E> set = map.get(type);
        return set == null
                ? 0
                : set.size();
    }

    @Override
    public int getOutdegree(N node, T type) {
        Map<T, Set<E>> map = outEdges.get(node);
        if(map == null) {
            return -1;
        }
        Set<E> set = map.get(type);
        return set == null
                ? 0
                : set.size();
    }

    @Override
    public Collection<? extends E> getOutEdges(N node, T type) {
        Map<T, Set<E>> map = outEdges.get(node);
        if(map == null) {
            return null;
        }
        return map.get(type);
    }

    @Override
    public Collection<? extends E> getInEdges(N node, T type) {
        Map<T, Set<E>> map = inEdges.get(node);
        if(map == null) {
            return null;
        }
        return map.get(type);
    }

    @Override
    public boolean hasNode(N node) {
        return nodes.contains(node);
    }

    @Override
    public boolean hasEdge(N source, N target, T type) {
        return getEdge(source, target, type) != null;
    }

    @Override
    public boolean hasEdge(E edge, T type) {
        Set<E> edges = this.edges.get(type);
        return edges != null && edges.contains(edge);
    }

    protected boolean addIfNotExists(N node) {
        return nodes.add(node);
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(!addIfNotExists(node)) {
            throw new NodeAlreadyExistsException();
        }
    }

    protected void addEdgeTo(N node, E edge, T type, boolean out) {
        Map<T, Set<E>> edgeMap = out
                ? outEdges.computeIfAbsent(node, _node -> typeEdgeMapSupplier.get())
                : inEdges.computeIfAbsent(node, _node -> typeEdgeMapSupplier.get());
        Set<E> edges = edgeMap.computeIfAbsent(type, _type -> edgeSetSupplier.get());
        edges.add(edge);
    }

    @Override
    public void addEdge(E edge, T type) throws EdgeAlreadyExistsException {
        if(hasEdge(edge, type)) {
            throw new EdgeAlreadyExistsException();
        }
        N source = edge.getSource();
        N target = edge.getTarget();
        addIfNotExists(source);
        addIfNotExists(target);
        addEdgeTo(source, edge, type, true);
        addEdgeTo(target, edge, type, false);
        Set<E> edges = this.edges.computeIfAbsent(type, _type -> edgeSetSupplier.get());
        edges.add(edge);
        Map<N, Map<T, E>> map = nodeEdgeMapping.computeIfAbsent(source, _source -> nodeEdgeMapSupplier.get());
        Map<T, E> map1 = map.computeIfAbsent(target, _target -> edgeMapSupplier.get());
        map1.put(type, edge);
    }

    protected void removeAllEdges(Map<T, Set<E>> edges) {
        if(edges == null) {
            return;
        }
        for(Map.Entry<T, Set<E>> edgesEntry: edges.entrySet()) {
            Set<E> thisEdges = this.edges.get(edgesEntry.getKey());
            if(thisEdges != null) {
                thisEdges.removeAll(edgesEntry.getValue());
            }
        }
    }

    @Override
    public boolean removeNode(N node) {
        if(nodes.remove(node)) {
            removeAllEdges(outEdges.remove(node));
            removeAllEdges(inEdges.remove(node));
            nodeEdgeMapping.remove(node);
            for(Map<N, Map<T, E>> map: nodeEdgeMapping.values()) {
                map.remove(node);
            }
            return true;
        }
        return false;
    }

    protected void removeEdge(Map<N, Map<T, Set<E>>> edgeMap, N node, E edge, T type) {
        Map<T, Set<E>> map = edgeMap.get(node);
        if(map != null) {
            Set<E> set = map.get(type);
            if(set != null) {
                set.remove(edge);
            }
        }
    }

    @Override
    public boolean removeEdge(N source, N target, T type) {
        E edge = getEdge(source, target, type);
        if(edge == null) {
            return false;
        }
        return removeEdge(edge, type);
    }

    @Override
    public boolean removeEdge(E edge, T type) {
        Set<E> edges = this.edges.get(type);
        if(edges != null && edges.remove(edge)) {
            removeEdge(outEdges, edge.getSource(), edge, type);
            removeEdge(inEdges, edge.getTarget(), edge, type);
            Map<N, Map<T, E>> map = nodeEdgeMapping.get(edge.getSource());
            if(map != null) {
                Map<T, E> map1 = map.get(edge.getTarget());
                if(map1 != null) {
                    map1.remove(type);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public E getEdge(N source, N target, T type) {
        Map<N, Map<T, E>> map = nodeEdgeMapping.get(source);
        if(map == null) {
            return null;
        }
        Map<T, E> map1 = map.get(target);
        if(map1 == null) {
            return null;
        }
        return map1.get(type);
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public boolean isUndirected() {
        return false;
    }
}
