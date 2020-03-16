package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

/**
 * @author Daniel Abitz
 */
public interface Graph<N extends Node, E extends Edge<N>> {

    boolean hasNode(N node);

    boolean hasEdge(E edge);

    boolean hasEdge(N source, N target);

    E getEdge(N source, N target);

    void addNode(N node) throws NodeAlreadyExistsException;

    void addEdge(E edge) throws EdgeAlreadyExistsException;

    void setEdge(E edge);

    boolean removeNode(N node);

    boolean removeEdge(N source, N target);

    boolean removeEdge(E edge);

    boolean isDirected();

    boolean isUndirected();
}
