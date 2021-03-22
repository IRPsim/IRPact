package de.unileipzig.irpact.commons.eval;

import de.unileipzig.irpact.commons.IsEquals;

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

    @Override
    public int getHashCode() {
        return IsEquals.DEFAULT_HASH;
    }
}