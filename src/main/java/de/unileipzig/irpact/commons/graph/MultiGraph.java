package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface MultiGraph<N extends Node, E extends Edge<N>, T> extends GraphBase<N, E> {

    Collection<? extends N> getNodes();

    Collection<? extends E> getEdges(T type);

    Collection<? extends E> getOutEdges(N node, T type);

    Collection<? extends E> getInEdges(N node, T type);

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
