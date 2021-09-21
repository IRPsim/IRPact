package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class Sub implements Eval {

    protected double value;

    public Sub() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double calculate(double input) {
        return input - value;
    }
}
