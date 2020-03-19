package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;

/**
 * @author Daniel Abitz
 */
public interface MultiGraph<N extends Node, E extends Edge<N>, T> extends GraphBase<N, E> {

    boolean hasNode(N node);

    boolean hasEdge(E edge, T type);

    boolean hasEdge(N source, N target, T type);

    E getEdge(N source, N target, T type);

    void addNode(N node) throws NodeAlreadyExistsException;

    void addEdge(E edge, T type) throws EdgeAlreadyExistsException;

    boolean removeNode(N node);

    boolean removeEdge(N source, N target, T type);

    boolean removeEdge(E edge, T type);
}
