package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

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

    protected boolean addIfNotExists(N node) {
        if(hasNode(node)) {
            return false;
        }
        graphData.put(node, createSubMap());
        return true;
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
        if(edgeData.containsKey(type) || edgeData.containsValue(edge)) {
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
        return edgeData.remove(type) != null;
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
