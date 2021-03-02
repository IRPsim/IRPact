package de.unileipzig.irpact.commons.eval;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public class Inverse implements Eval {

    public Inverse() {
    }

    @Override
    public double evaluate(double x) {
        return 1.0 / x;
    }

    @Override
    public int getHashCode() {
        return IsEquals.DEFAULT_HASH;
    }
}
