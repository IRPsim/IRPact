package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class Add implements Eval {

    protected double value;

    public Add() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double calculate(double input) {
        return input + value;
    }
}
