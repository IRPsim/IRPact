package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class Constant implements Eval {

    protected double value;

    public Constant() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double calculate(double input) {
        return value;
    }
}
