package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public interface BoundedEval extends Eval {

    double getUpperBound();

    boolean isValid(double input);
}
