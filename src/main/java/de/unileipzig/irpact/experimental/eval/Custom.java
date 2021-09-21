package de.unileipzig.irpact.experimental.eval;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

/**
 * @author Daniel Abitz
 */
public class Custom implements BoundedEval {

    protected String expString;
    protected double upperBound;

    public Custom() {
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public boolean isValid(double input) {
        return input < upperBound;
    }

    public void setExpressionString(String expString) {
        this.expString = expString;
    }

    @Override
    public double calculate(double input) {
        return new Expression(expString, new Argument("x = " + input)).calculate();
    }
}
