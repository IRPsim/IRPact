package de.unileipzig.irpact.commons.spatial;

import de.unileipzig.irpact.commons.eval.Eval;
import de.unileipzig.irpact.commons.eval.NoDistance;

/**
 * @author Daniel Abitz
 */
public class BasicDistanceEvaluator implements DistanceEvaluator, Eval {

    protected Eval eval;

    public BasicDistanceEvaluator() {
    }

    public BasicDistanceEvaluator(Eval eval) {
        this.eval = eval;
    }

    public void setEval(Eval eval) {
        this.eval = eval;
    }

    public Eval getEval() {
        return eval;
    }

    @Override
    public boolean isDisabled() {
        return eval instanceof NoDistance;
    }

    @Override
    public double evaluate(double distance) {
        return eval.evaluate(distance);
    }

    @Override
    public int getHashCode() {
        return eval.getHashCode();
    }
}
