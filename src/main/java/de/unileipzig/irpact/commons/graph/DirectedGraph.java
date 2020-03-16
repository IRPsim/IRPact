package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.EdgeNodesMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class DirectedGraph<N extends Node, E extends Edge<N>> implements Graph<N, E> {

    protected Map<N, Map<N, E>> graphData = new HashMap<>();

    public DirectedGraph() {
    }

    protected Map<N, E> getLinkedVertices(N source) {
        return graphData.computeIfAbsent(source, _source -> new HashMap<>());
    }

    protected boolean addIfNotExists(N node) {
        if(!hasNode(node)) {
            graphData.put(node, new HashMap<>());
            return true;
        }
        return false;
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
        return graphData.containsKey(node);
    }

    @Override
    public boolean hasEdge(E edge) {
        return hasEdge(edge.getSource(), edge.getTarget());
    }

    @Override
    public boolean hasEdge(N source, N target) {
        return getLinkedVertices(source).containsKey(target);
    }

    @Override
    public E getEdge(N source, N target) {
        Map<N, E> linked = getLinkedVertices(source);
        return linked.get(target);
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(hasNode(node)) {
            throw new NodeAlreadyExistsException(node.getLabel());
        }
        graphData.put(node, new HashMap<>());
    }

    protected void validateEdgeNodes(N source, N target, E edge) {
        if(source != edge.getSource()) {
            throw new EdgeNodesMismatchException("source");
        }
        if(target != edge.getTarget()) {
            throw new EdgeNodesMismatchException("target");
        }
    }

    protected void addEdge(N source, N target, E edge) throws EdgeAlreadyExistsException {
        validateEdgeNodes(source, target, edge);
        Map<N, E> linked = getLinkedVertices(source);
        if(linked.containsKey(target)) {
            throw new EdgeAlreadyExistsException();
        }
        linked.put(target, edge);
        addIfNotExists(target);
    }

    @Override
    public void addEdge(E edge) throws EdgeAlreadyExistsException {
        N source = edge.getSource();
        N target = edge.getTarget();
        addEdge(source, target, edge);
    }

    protected void setEdge(N source, N target, E edge) {
        validateEdgeNodes(source, target, edge);
        Map<N, E> linked = getLinkedVertices(source);
        linked.put(target, edge);
        addIfNotExists(target);
    }

    @Override
    public void setEdge(E edge) {
        N source = edge.getSource();
        N target = edge.getTarget();
        setEdge(source, target, edge);
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
        Map<N, E> linked = getLinkedVertices(source);
        return linked.remove(target) != null;
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
}
