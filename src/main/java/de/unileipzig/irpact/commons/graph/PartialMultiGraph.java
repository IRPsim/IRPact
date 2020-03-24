package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Beschraenkt einen Multigraph auf einen Typ und ermoeglicht so die Nutzung als einfachen Graph.
 * Alle Methoden werden direkt an den Master-Graphen weitergeleitet.
 * @author Daniel Abitz
 */
public class PartialMultiGraph<N extends Node, E extends Edge<N>, T> implements Graph<N, E> {

    protected T type;
    protected MultiGraph<N, E, T> multiGraph;

    public PartialMultiGraph(T type, MultiGraph<N, E, T> multiGraph) {
        this.type = type;
        this.multiGraph = multiGraph;
    }

    public T getType() {
        return type;
    }

    @Override
    public Set<E> getEdges() {
        return multiGraph.getEdges(type);
    }

    @Override
    public Set<N> getSourceNodes(N targetNode) {
        return multiGraph.getSourceNodes(targetNode, type);
    }

    @Override
    public Stream<N> streamSourceNodes(N targetNode) {
        return multiGraph.streamSourceNodes(targetNode, type);
    }

    @Override
    public Set<N> getTargetNodes(N sourceNode) {
        return multiGraph.getTargetNodes(sourceNode, type);
    }

    @Override
    public Set<E> getOutEdges(N node) {
        return multiGraph.getOutEdges(node, type);
    }

    @Override
    public Set<E> getInEdges(N node) {
        return multiGraph.getInEdges(node, type);
    }

    @Override
    public int getDegree(N node) {
        return multiGraph.getDegree(node, type);
    }

    @Override
    public int getIndegree(N node) {
        return multiGraph.getIndegree(node, type);
    }

    @Override
    public int getOutdegree(N node) {
        return multiGraph.getOutdegree(node, type);
    }

    @Override
    public boolean hasNode(N node) {
        return multiGraph.hasNode(node);
    }

    @Override
    public boolean hasEdge(E edge) {
        return multiGraph.hasEdge(edge, type);
    }

    @Override
    public boolean hasEdge(N source, N target) {
        return multiGraph.hasEdge(source, target, type);
    }

    @Override
    public E getEdge(N source, N target) {
        return multiGraph.getEdge(source, target, type);
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        multiGraph.addNode(node);
    }

    @Override
    public void addEdge(E edge) throws EdgeAlreadyExistsException {
        multiGraph.addEdge(edge, type);
    }

    @Override
    public boolean removeNode(N node) {
        return multiGraph.removeNode(node);
    }

    @Override
    public boolean removeEdge(N source, N target) {
        return multiGraph.removeEdge(source, target, type);
    }

    @Override
    public boolean removeEdge(E edge) {
        return multiGraph.removeEdge(edge, type);
    }

    @Override
    public Set<N> getNodes() {
        return multiGraph.getNodes();
    }

    @Override
    public boolean isDirected() {
        return multiGraph.isDirected();
    }

    @Override
    public boolean isUndirected() {
        return multiGraph.isUndirected();
    }
}
