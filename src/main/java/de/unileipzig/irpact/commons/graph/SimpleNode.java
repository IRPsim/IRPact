package de.unileipzig.irpact.commons.graph;

/**
 * @author Daniel Abitz
 */
public class SimpleNode implements Node {

    private String label;

    public SimpleNode(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
