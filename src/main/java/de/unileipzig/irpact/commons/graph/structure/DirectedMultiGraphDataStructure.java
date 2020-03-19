package de.unileipzig.irpact.commons.graph.structure;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.Edge;
import de.unileipzig.irpact.commons.graph.Node;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface DirectedMultiGraphDataStructure<N extends Node, E extends Edge<N>, T>
        extends MultiGraphDataStructure<N, E, T> {

    Collection<? extends E> getOutEdges(N node, T type);

    Collection<? extends E> getInEdges(N node, T type);

    boolean hasNode(N node);

    boolean hasEdge(E edge, T type);

    void addNode(N node) throws NodeAlreadyExistsException;

    void addEdge(E edge, T type) throws EdgeAlreadyExistsException;

    boolean removeNode(N node);

    boolean removeEdge(E edge, T type);

    E getEdge(N source, N target, T type);
}
