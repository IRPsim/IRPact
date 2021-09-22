package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractBoundedEval implements BoundedEval {

    protected Eval intern;
    protected double upperBound;

    public AbstractBoundedEval() {
    }

    public void setIntern(Eval intern) {
        this.intern = intern;
    }

    public Eval getIntern() {
        return intern;
    }

    protected double calculateIntern(double value) {
        return getIntern().calculate(value);
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
}
