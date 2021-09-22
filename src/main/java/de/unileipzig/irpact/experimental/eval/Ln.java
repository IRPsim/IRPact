package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class Ln extends AbstractBoundedEval {

    @Override
    public double calculate(double input) {
        double x = calculateIntern(input);
        return Math.log(x);
    }
}
