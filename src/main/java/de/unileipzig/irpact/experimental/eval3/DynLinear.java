package de.unileipzig.irpact.experimental.eval3;

/**
 * @author Daniel Abitz
 */
public class DynLinear extends AbstractBoundedEval {

    protected double m = 1.0;
    protected double n = 0.0;

    public void setM(double m) {
        this.m = m;
    }

    public void setN(double n) {
        this.n = n;
    }

    @Override
    protected void init() {
    }

    @Override
    public double calculate(double value) {
        double x = calculateIntern(value);
        return y0 + m * xMaxInv * (x - x0) + n;
    }

    @Override
    public String toString() {
        return "'" + m + "' * x + '" + n;
    }
}