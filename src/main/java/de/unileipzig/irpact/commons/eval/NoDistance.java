package de.unileipzig.irpact.commons.eval;

/**
 * @author Daniel Abitz
 */
public class NoDistance implements Eval {

    public NoDistance() {
    }

    @Override
    public double evaluate(double x) {
        throw new UnsupportedOperationException("NoDistance");
    }
}
