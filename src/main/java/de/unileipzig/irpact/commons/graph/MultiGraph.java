package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.dev.ToDo;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface MultiGraph<N extends Node, E extends Edge<N>, T> extends GraphBase<N, E> {

    Set<E> getEdges(T type);

    Set<N> getSourceNodes(N targetNode, T type);

    Stream<N> streamSourceNodes(N targetNode, T type);

    Set<N> getTargetNodes(N sourceNode, T type);

    Set<E> getOutEdges(N node, T type);

    Set<E> getInEdges(N node, T type);

    int getDegree(N node, T type);

    int getIndegree(N node, T type);

    int getOutdegree(N node, T type);

    boolean hasNode(N node);

    boolean hasEdge(E edge, T type);

    boolean hasEdge(N source, N target, T type);

    E getEdge(N source, N target, T type);

    void addNode(N node) throws NodeAlreadyExistsException;

    @ToDo("ueberlegen, ob selbe edgeinstanz mehrmals hinzugefuegt werden darf fuer unterschiedliche typen")
    void addEdge(E edge, T type) throws EdgeAlreadyExistsException;

    boolean removeNode(N node);

    boolean removeEdge(N source, N target, T type);

    boolean removeEdge(E edge, T type);
}
