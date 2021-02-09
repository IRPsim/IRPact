package de.unileipzig.irpact.commons.eval;

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
}
