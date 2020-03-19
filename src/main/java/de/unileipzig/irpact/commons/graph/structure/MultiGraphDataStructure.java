package de.unileipzig.irpact.commons.graph.structure;

import de.unileipzig.irpact.commons.graph.Edge;
import de.unileipzig.irpact.commons.graph.Node;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface MultiGraphDataStructure<N extends Node, E extends Edge<N>, T> {

    Collection<? extends N> getNodes();

    Collection<? extends E> getEdges(T type);

    int getDegree(N node, T type);

    int getIndegree(N node, T type);

    int getOutdegree(N node, T type);
}
