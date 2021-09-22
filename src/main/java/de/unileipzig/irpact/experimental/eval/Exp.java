package de.unileipzig.irpact.experimental.eval;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Exp implements Eval {

    protected static final Comparator<BoundedEval> BOUND_COMPARATOR = Comparator.comparingDouble(BoundedEval::getUpperBound);

    protected NavigableSet<BoundedEval> ranges = new TreeSet<>(BOUND_COMPARATOR);

    public Exp() {
    }

    public void rearrange() {
        NavigableSet<BoundedEval> temp = new TreeSet<>(BOUND_COMPARATOR);
        temp.addAll(ranges);
        ranges.clear();
        ranges.addAll(temp);
    }

    public boolean hasEvalWithSameBound(BoundedEval eval) {
        for(BoundedEval thisEval: ranges) {
            if(thisEval.getUpperBound() == eval.getUpperBound()) {
                return true;
            }
        }
        return false;
    }

    public boolean add(BoundedEval eval) {
        if(hasEvalWithSameBound(eval)) {
            return false;
        }
        ranges.add(eval);
        return true;
    }

    protected BoundedEval find(double input) {
        Iterator<BoundedEval> iter = ranges.iterator();
        BoundedEval next;
        while(iter.hasNext()) {
            next = iter.next();
            if(next.isValid(input) || !iter.hasNext()) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public double calculate(double input) {
        if(ranges.isEmpty()) {
            throw new IllegalStateException("empty");
        }
        return find(input).calculate(input);
    }
}
