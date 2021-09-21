package de.unileipzig.irpact.experimental.eval3;

/**
 * @author Daniel Abitz
 */
public class Exponential extends AbstractBoundedEval {

    protected double N0 = 1.0;
    protected double lambda = -1.0;

    public void setN0(double n0) {
        this.N0 = n0;
    }

    public double getN0() {
        return N0;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getLambda() {
        return lambda;
    }

    @Override
    protected void init() {
    }

    @Override
    public double calculate(double x) {
        return N0 * Math.exp(lambda * x);
    }
}