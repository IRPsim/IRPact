package de.unileipzig.irpact.commons.spatial;

import de.unileipzig.irpact.commons.eval.Eval;

/**
 * @author Daniel Abitz
 */
public class BasicDistanceEvaluator implements DistanceEvaluator, Eval {

    protected Eval eval;

    public BasicDistanceEvaluator(Eval eval) {
        this.eval = eval;
    }

    @Override
    public double evaluate(double distance) {
        return eval.evaluate(distance);
    }
}
