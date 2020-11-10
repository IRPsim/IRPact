package de.unileipzig.irpact.v2.pg.eval;

/**
 * @author Daniel Abitz
 */
public class Par2 implements Eval {

    protected double a = 1.0;
    protected double b = 0.0;
    protected double c = 0.0;

    public Par2() {
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public double calculate(double input) {
        return a * input * input + b * input + c;
    }
}
