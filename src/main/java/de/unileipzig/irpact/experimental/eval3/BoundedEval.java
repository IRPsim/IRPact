package de.unileipzig.irpact.experimental.eval3;

/**
 * @author Daniel Abitz
 */
public interface BoundedEval extends Eval {

    void init(double x0, double y0);

    boolean isValid(double x);

    double getX1();

    double calculateY1();
}
