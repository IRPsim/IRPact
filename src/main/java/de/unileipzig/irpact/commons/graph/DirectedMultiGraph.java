package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class DirectedMultiGraph<N extends Node, E extends Edge<N>, T> implements MultiGraph<N, E, T> {

    protected Map<N, Map<N, Map<T, E>>> graphData;

    public DirectedMultiGraph(Map<N, Map<N, Map<T, E>>> graphData) {
        this.graphData = graphData;
    }

    protected Map<N, Map<T, E>> createSubMap() {
        return new HashMap<>();
    }

    protected Map<T, E> createEdgeMap() {
        return new HashMap<>();
    }

    protected Set<E> createEdgeSet() {
        return new HashSet<>();
    }

    protected boolean scanNode(N node) {
        if(graphData.containsKey(node)) return true;
        for(Map<N, Map<T, E>> map: graphData.values()) {
            if(map.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge) {
        for(Map<N, Map<T, E>> map: graphData.values()) {
            for(Map<T, E> map1: map.values()) {
                if(map1.containsValue(edge)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge, T type) {
        for(Map<N, Map<T, E>> map: graphData.values()) {
            for(Map<T, E> map1: map.values()) {
                E e = map1.get(type);
                if(e != null && e == edge) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Collection<? extends N> getNodes() {
        return graphData.keySet();
    }

    @Override
    public Collection<? extends E> getEdges(T type) {
        Set<E> edges = createEdgeSet();
        for(Map<N, Map<T, E>> map: graphData.values()) {
            for(Map<T, E> map1: map.values()) {
                E edge = map1.get(type);
                if(edge != null) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    @Override
    public Collection<? extends E> getInEdges(N node, T type) {
        if(!hasNode(node)) {
            return null;
        }
        Set<E> edges = createEdgeSet();
        for(Map<N, Map<T, E>> map: graphData.values()) {
            Map<T, E> map1 = map.get(node);
            if(map1 != null) {
                E e = map1.get(type);
                if(e != null) {
                    edges.add(e);
                }
            }
        }
        return edges;
    }

    @Override
    public Collection<? extends E> getOutEdges(N node, T type) {
        Map<N, Map<T, E>> map = graphData.get(node);
        if(map == null) {
            return null;
        }
        Set<E> edges = createEdgeSet();
        for(Map<T, E> map1: map.values()) {
            E e = map1.get(type);
            if(e != null) {
                edges.add(e);
            }
        }
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
        for(Map<N, Map<T, E>> map: graphData.values()) {
            Map<T, E> map1 = map.get(node);
            if(map1 != null) {
                E e = map1.get(type);
                if(e != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int getOutdegree(N node, T type) {
        Map<N, Map<T, E>> map = graphData.get(node);
        if(map == null) {
            return -1;
        }
        int count = 0;
        for(Map<T, E> map1: map.values()) {
            if(map1.containsKey(type)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean hasNode(N node) {
        if(graphData.containsKey(node)) {
            return true;
        }
        for(Map<N, Map<T, E>> subData: graphData.values()) {
            if(subData.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasEdge(E edge, T type) {
        return hasEdge(edge.getSource(), edge.getTarget(), type);
    }

    @Override
    public boolean hasEdge(N source, N target, T type) {
        Map<N, Map<T, E>> subData = graphData.get(source);
        if(subData == null) {
            return false;
        }
        Map<T, E> edgeData = subData.get(target);
        if(edgeData == null) {
            return false;
        }
        return edgeData.containsKey(type);
    }

    @Override
    public E getEdge(N source, N target, T type) {
        Map<N, Map<T, E>> subData = graphData.get(source);
        if(subData == null) {
            return null;
        }
        Map<T, E> edgeData = subData.get(target);
        if(edgeData == null) {
            return null;
        }
        return edgeData.get(type);
    }

    protected boolean addIfNotExists(N node) {
        if(hasNode(node)) {
            return false;
        }
        graphData.put(node, createSubMap());
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
        Map<N, Map<T, E>> subData = graphData.get(source);
        Map<T, E> edgeData = subData.computeIfAbsent(target, _target -> createEdgeMap());
        //if(edgeData.containsKey(type) || edgeData.containsValue(edge)) {
        if(edgeData.containsKey(type)) { //siehe to_do im interface
            throw new EdgeAlreadyExistsException();
        }
        edgeData.put(type, edge);
    }

    @Override
    public boolean removeNode(N node) {
        if(graphData.remove(node) != null) {
            for(Map<N, Map<T, E>> subData: graphData.values()) {
                subData.remove(node);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeEdge(N source, N target, T type) {
        Map<N, Map<T, E>> subData = graphData.get(source);
        if(subData == null) {
            return false;
        }
        Map<T, E> edgeData = subData.get(target);
        if(edgeData == null) {
            return false;
        }
        return edgeData.remove(type) != null; //empty edgeData bleibt erstmal erhalten
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
