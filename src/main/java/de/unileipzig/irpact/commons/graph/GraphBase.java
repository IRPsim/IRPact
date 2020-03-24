package de.unileipzig.irpact.commons.graph;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface GraphBase<N extends Node, E extends Edge<N>> {

    Set<N> getNodes();

    boolean isDirected();

    boolean isUndirected();
}
