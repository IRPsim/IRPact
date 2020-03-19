package de.unileipzig.irpact.commons.graph;

/**
 * @author Daniel Abitz
 */
public interface GraphBase<N extends Node, E extends Edge<N>> {

    boolean isDirected();

    boolean isUndirected();

    default int getDegree(N node) {
        return getIndegree(node) + getOutdegree(node);
    }

    int getIndegree(N node);

    int getOutdegree(N node);
}
