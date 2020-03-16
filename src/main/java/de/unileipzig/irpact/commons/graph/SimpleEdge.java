package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.Check;

/**
 * @author Daniel Abitz
 */
public class SimpleEdge<N extends Node> implements Edge<N> {

    protected N source;
    protected N target;
    protected String label;

    public SimpleEdge(N source, N target, String label) {
        this.source = Check.requireNonNull(source, "source");
        this.target = Check.requireNonNull(target, "target");
        this.label = Check.requireNonNull(label, "label");
    }

    @Override
    public N getSource() {
        return source;
    }

    @Override
    public N getTarget() {
        return target;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
