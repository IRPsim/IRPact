package de.unileipzig.irpact.commons.graph;

/**
 * @author Daniel Abitz
 */
public interface Edge<N extends Node> {

    N getSource();

    N getTarget();

    String getLabel();
}
