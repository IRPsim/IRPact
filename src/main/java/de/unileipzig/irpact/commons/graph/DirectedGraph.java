package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class DirectedGraph<N extends Node, E extends Edge<N>> implements Graph<N, E> {

    protected Map<N, Map<N, E>> graphData;

    public DirectedGraph() {
        this(new HashMap<>());
    }

    public DirectedGraph(Map<N, Map<N, E>> graphData) {
        this.graphData = graphData;
    }

    protected Map<N, E> createSubMap() {
        return new HashMap<>();
    }

    protected boolean addIfNotExists(N node) {
        if(hasNode(node)) {
            return false;
        }
        graphData.put(node, createSubMap());
        return true;
    }

    protected boolean scan(N node) {
        if(graphData.containsKey(node)) {
            return true;
        }
        for(Map<N, E> linked: graphData.values()) {
            if(linked.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasNode(N node) {
        if(graphData.containsKey(node)) {
            return true;
        }
        for(Map<N, E> subData: graphData.values()) {
            if(subData.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasEdge(E edge) {
        return hasEdge(edge.getSource(), edge.getTarget());
    }

    @Override
    public boolean hasEdge(N source, N target) {
        Map<N, E> sub = graphData.get(source);
        if(sub == null) {
            return false;
        }
        return sub.containsKey(target);
    }

    @Override
    public E getEdge(N source, N target) {
        Map<N, E> sub = graphData.get(source);
        if(sub == null) {
            return null;
        }
        return sub.get(target);
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(!addIfNotExists(node)) {
            throw new NodeAlreadyExistsException();
        }
    }

    @Override
    public void addEdge(E edge) throws EdgeAlreadyExistsException {
        N source = edge.getSource();
        N target = edge.getTarget();
        addIfNotExists(source);
        addIfNotExists(target);
        Map<N, E> sub = graphData.get(source);
        if(sub.containsKey(target)) {
            throw new EdgeAlreadyExistsException();
        }
        sub.put(target, edge);
    }

    @Override
    public boolean removeNode(N node) {
        if(graphData.remove(node) != null) {
            for(Map<N, E> linked: graphData.values()) {
                linked.remove(node);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeEdge(N source, N target) {
        Map<N, E> sub = graphData.get(source);
        if(sub == null) {
            return false;
        }
        return sub.remove(target) != null;
    }

    @Override
    public boolean removeEdge(E edge) {
        N source = edge.getSource();
        N target = edge.getTarget();
        return removeEdge(source, target);
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public boolean isUndirected() {
        return false;
    }

    @Override
    public int getOutdegree(N node) {
        Map<N, E> out = graphData.get(node);
        return out == null
                ? 0
                : out.size();
    }

    @Override
    public int getIndegree(N node) {
        int count = 0;
        for(Map<N, E> in: graphData.values()) {
            if(in.containsKey(node)) {
                count++;
            }
        }
        return count;
    }
}
