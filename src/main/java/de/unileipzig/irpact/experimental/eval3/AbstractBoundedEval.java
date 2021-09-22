package de.unileipzig.irpact.experimental.eval3;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractBoundedEval implements BoundedEval {

    protected Eval intern;
    protected double xMax = 1.0;
    protected double xMaxInv = 1.0;
    protected double x0;
    protected double y0;
    protected double x1;

    public AbstractBoundedEval() {
    }

    public void setXMax(double xMax) {
        this.xMax = xMax;
        xMaxInv = 1.0 / xMax;
    }

    public double getXMax() {
        return xMax;
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    @Override
    public double getX1() {
        return x1;
    }

    @Override
    public double calculateY1() {
        return calculate(getX1());
    }

    public void setIntern(Eval intern) {
        this.intern = intern;
    }

    public Eval getIntern() {
        return intern;
    }

    public boolean hasIntern() {
        return intern != null;
    }

    protected double calculateIntern(double value) {
        Eval intern = getIntern();
        return intern == null
                ? value
                : intern.calculate(value);
    }

    protected abstract void init();

    @Override
    public void init(double x0, double y0) {
        this.x0 = x0;
        this.y0 = y0;
        init();
    }

    @Override
    public boolean isValid(double x) {
        return x0 <= x && x < x1;
    }
}
