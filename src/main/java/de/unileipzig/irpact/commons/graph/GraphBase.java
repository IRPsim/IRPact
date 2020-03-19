package de.unileipzig.irpact.commons.graph;

/**
 * @author Daniel Abitz
 */
public interface GraphBase<N extends Node, E extends Edge<N>> {

    boolean isDirected();

    boolean isUndirected();
}
