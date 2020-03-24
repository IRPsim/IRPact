package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class DirectedGraph<N extends Node, E extends Edge<N>> implements Graph<N, E> {

    protected Supplier<? extends Map<N, Map<N, E>>> graphDataSupplier;
    protected Supplier<? extends Map<N, E>> mapSupplier;
    protected Supplier<? extends Set<N>> nodeSetSupplier;
    protected Supplier<? extends Set<E>> edgeSetSupplier;
    protected Map<N, Map<N, E>> graphData;

    public DirectedGraph(Map<N, Map<N, E>> graphData) {
        this(
                HashMap::new,
                HashMap::new,
                HashSet::new,
                HashSet::new,
                graphData
        );
    }

    public DirectedGraph(
            Supplier<? extends Map<N, Map<N, E>>> graphDataSupplier,
            Supplier<? extends Map<N, E>> mapSupplier,
            Supplier<? extends Set<N>> nodeSetSupplier,
            Supplier<? extends Set<E>> edgeSetSupplier,
            Map<N, Map<N, E>> graphData) {
        this.graphDataSupplier = graphDataSupplier;
        this.mapSupplier = mapSupplier;
        this.nodeSetSupplier = nodeSetSupplier;
        this.edgeSetSupplier = edgeSetSupplier;
        this.graphData = graphData;
    }

    protected boolean addIfNotExists(N node) {
        if(hasNode(node)) {
            return false;
        }
        graphData.put(node, mapSupplier.get());
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
    public Set<N> getNodes() {
        return graphData.keySet();
    }

    @Override
    public Set<E> getEdges() {
        Set<E> edges = edgeSetSupplier.get();
        for(Map<N, E> map: graphData.values()) {
            edges.addAll(map.values());
        }
        return edges;
    }

    @Override
    public Set<N> getSourceNodes(N targetNode) {
        if(!hasNode(targetNode)) {
            return null;
        }
        Set<N> nodes = nodeSetSupplier.get();
        for(Map<N, E> edges: graphData.values()) {
            E e = edges.get(targetNode);
            if(e != null) {
                nodes.add(e.getSource());
            }
        }
        return nodes;
    }

    @Override
    public Stream<N> streamSourceNodes(N targetNode) {
        if(!hasNode(targetNode)) {
            return Stream.empty();
        }
        return graphData.values()
                .stream()
                .filter(_map -> _map.get(targetNode) != null)
                .map(_map -> _map.get(targetNode).getSource());
    }

    @Override
    public Set<N> getTargetNodes(N sourceNode) {
        Map<N, E> edges = graphData.get(sourceNode);
        if(edges == null) {
            return null;
        }
        Set<N> targetNodes = nodeSetSupplier.get();
        targetNodes.addAll(edges.keySet());
        return targetNodes;
    }

    @Override
    public Set<E> getInEdges(N node) {
        if(!hasNode(node)) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        for(Map<N, E> map: graphData.values()) {
            E e = map.get(node);
            if(e != null) {
                edges.add(e);
            }
        }
        return edges;
    }

    @Override
    public Set<E> getOutEdges(N node) {
        Map<N, E> map = graphData.get(node);
        if(map == null) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        edges.addAll(map.values());
        return edges;
    }

    @Override
    public int getDegree(N node) {
        int in = getIndegree(node);
        int out = getOutdegree(node);
        if(in == -1 && out == -1) {
            return -1;
        }
        return (in == -1 ? 0 : in)
                + (out == -1 ? 0 : out);
    }

    @Override
    public int getIndegree(N node) {
        if(!hasNode(node)) {
            return -1;
        }
        int count = 0;
        for(Map<N, E> map: graphData.values()) {
            E e = map.get(node);
            if(e != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getOutdegree(N node) {
        Map<N, E> map = graphData.get(node);
        return map == null
                ? -1
                : map.values().size();
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
}
