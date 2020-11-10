package de.unileipzig.irpact.v2.pg.eval;

/**
 * @author Daniel Abitz
 */
public class Par1 implements Eval {

    protected double a = 1.0;
    protected double b = 0.0;

    @Override
    public double calculate(double input) {
        return a * input + b;
    }
}
