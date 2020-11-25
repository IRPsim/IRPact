package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class Log extends AbstractBoundedEval {

    protected double base;

    public Log() {
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getBase() {
        return base;
    }

    @Override
    public double calculate(double input) {
        double x = calculateIntern(input);
        return Math.log(x) / Math.log(base);
    }
}
