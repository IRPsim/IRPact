package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class DirectedMultiGraph2<N extends Node, E extends Edge<N>, T> implements MultiGraph<N, E, T> {

    protected Supplier<? extends Map<N, Map<N, E>>> subMapSupplier;
    protected Supplier<? extends Map<N, E>> edgeMapSupplier;
    protected Supplier<? extends Set<N>> nodeSetSupplier;
    protected Supplier<? extends Set<E>> edgeSetSupplier;
    protected Set<N> nodeData;
    protected Map<T, Map<N, Map<N, E>>> edgeData;

    public DirectedMultiGraph2(
            Set<N> nodeData,
            Map<T, Map<N, Map<N, E>>> edgeData) {
        this(
                HashMap::new,
                HashMap::new,
                HashSet::new,
                HashSet::new,
                nodeData,
                edgeData
        );
    }

    public DirectedMultiGraph2(
            Supplier<? extends Map<N, Map<N, E>>> subMapSupplier,
            Supplier<? extends Map<N, E>> edgeMapSupplier,
            Supplier<? extends Set<N>> nodeSetSupplier,
            Supplier<? extends Set<E>> edgeSetSupplier,
            Set<N> nodeData,
            Map<T, Map<N, Map<N, E>>> edgeData) {
        this.subMapSupplier = subMapSupplier;
        this.edgeMapSupplier = edgeMapSupplier;
        this.nodeSetSupplier = nodeSetSupplier;
        this.edgeSetSupplier = edgeSetSupplier;
        this.nodeData = nodeData;
        this.edgeData = edgeData;
    }

    protected boolean scanNode(N node) {
        if(nodeData.contains(node)) return true;
        for(Map<N, Map<N, E>> map: edgeData.values()) {
            if(map.containsKey(node)) {
                return true;
            }
            for(Map<N, E> map1: map.values()) {
                if(map1.containsKey(node)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge) {
        for(Map<N, Map<N, E>> map: edgeData.values()) {
            for(Map<N, E> map1: map.values()) {
                if(map1.containsValue(edge)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge, T type) {
        Map<N, Map<N, E>> map = edgeData.get(type);
        for(Map<N, E> map1: map.values()) {
            if(map1.containsValue(edge)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<N> getNodes() {
        return nodeData;
    }

    @Override
    public Set<E> getEdges(T type) {
        Set<E> edges = edgeSetSupplier.get();
        Map<N, Map<N, E>> map = edgeData.get(type);
        for(Map<N, E> map1: map.values()) {
            edges.addAll(map1.values());
        }
        return edges;
    }

    @Override
    public Set<N> getSourceNodes(N targetNode, T type) {
        if(!hasNode(targetNode)) {
            return null;
        }
        Set<N> nodes = nodeSetSupplier.get();
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map != null) {
            for(Map<N, E> map1: map.values()) {
                E e = map1.get(targetNode);
                if(e != null) {
                    nodes.add(e.getSource());
                }
            }
        }
        return nodes;
    }

    @Override
    public Stream<N> streamSourceNodes(N targetNode, T type) {
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return Stream.empty();
        }
        return map.values()
                .stream()
                .flatMap(_map -> _map.values().stream())
                .filter(_edge -> _edge.getTarget() == targetNode)
                .map(Edge::getSource);
    }

    @Override
    public Set<N> getTargetNodes(N sourceNode, T type) {
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return null;
        }
        Map<N, E> map1 = map.get(sourceNode);
        if(map1 == null) {
            return null;
        }
        Set<N> nodes = nodeSetSupplier.get();
        nodes.addAll(map1.keySet());
        return nodes;
    }

    @Override
    public Set<E> getInEdges(N node, T type) {
        if(!hasNode(node)) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return null;
        }
        for(Map<N, E> map1: map.values()) {
            E e = map1.get(node);
            if(e != null) {
                edges.add(e);
            }
        }
        return edges;
    }

    @Override
    public Set<E> getOutEdges(N node, T type) {
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        Map<N, E> map1 = map.get(node);
        if(map1 == null) {
            return null;
        }
        edges.addAll(map1.values());
        return edges;
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
        if(!hasNode(node)) {
            return -1;
        }
        int count = 0;
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return -1;
        }
        for(Map<N, E> map1: map.values()) {
            E e = map1.get(node);
            if(e != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getOutdegree(N node, T type) {
        Map<N, Map<N, E>> map = edgeData.get(type);
        if(map == null) {
            return -1;
        }
        Map<N, E> map1 = map.get(node);
        if(map1 == null) {
            return -1;
        }
        return map1.size();
    }

    @Override
    public boolean hasNode(N node) {
        return nodeData.contains(node);
    }

    @Override
    public boolean hasEdge(E edge, T type) {
        return hasEdge(edge.getSource(), edge.getTarget(), type);
    }

    @Override
    public boolean hasEdge(N source, N target, T type) {
        Map<N, Map<N, E>> subData = edgeData.get(type);
        if(subData == null) {
            return false;
        }
        Map<N, E> edgeData = subData.get(source);
        if(edgeData == null) {
            return false;
        }
        return edgeData.containsKey(target);
    }

    @Override
    public E getEdge(N source, N target, T type) {
        Map<N, Map<N, E>> subData = edgeData.get(type);
        if(subData == null) {
            return null;
        }
        Map<N, E> edgeData = subData.get(source);
        if(edgeData == null) {
            return null;
        }
        return edgeData.get(target);
    }

    protected boolean addIfNotExists(N node) {
        if(hasNode(node)) {
            return false;
        }
        nodeData.add(node);
        return true;
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(!addIfNotExists(node)) {
            throw new NodeAlreadyExistsException();
        }
    }

    @Override
    public void addEdge(E edge, T type) throws EdgeAlreadyExistsException {
        N source = edge.getSource();
        N target = edge.getTarget();
        addIfNotExists(source);
        addIfNotExists(target);
        Map<N, Map<N, E>> subData = edgeData.computeIfAbsent(type, _type -> subMapSupplier.get());
        Map<N, E> edgeData = subData.computeIfAbsent(source, _source -> edgeMapSupplier.get());
        //if(edgeData.containsKey(type) || edgeData.containsValue(edge)) {
        if(edgeData.containsKey(target)) { //siehe to_do im interface
            throw new EdgeAlreadyExistsException();
        }
        edgeData.put(target, edge);
    }

    @Override
    public boolean removeNode(N node) {
        if(nodeData.remove(node)) {
            for(Map<N, Map<N, E>> subData: edgeData.values()) {
                for(Map<N, E> edgeData: subData.values()) {
                    edgeData.remove(node);
                }
                subData.remove(node);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeEdge(N source, N target, T type) {
        Map<N, Map<N, E>> subData = edgeData.get(type);
        if(subData == null) {
            return false;
        }
        Map<N, E> edgeData = subData.get(source);
        if(edgeData == null) {
            return false;
        }
        return edgeData.remove(target) != null; //empty edgeData bleibt erstmal erhalten
    }

    @Override
    public boolean removeEdge(E edge, T type) {
        N source = edge.getSource();
        N target = edge.getTarget();
        return removeEdge(source, target, type);
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
