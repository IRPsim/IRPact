package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class DoubleRange extends NameableBase {

    protected double lowerBound;
    protected double upperBound;
    protected boolean lowerBoundInclusive;
    protected boolean upperBoundInclusive;

    public DoubleRange() {
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean isInRange(
            double x,
            double lowerBound, boolean lowerBoundInclusive,
            double upperBound, boolean upperBoundInclusive) {
        if(lowerBoundInclusive && x < lowerBound) return false;
        if(!lowerBoundInclusive && x <= lowerBound) return false;
        if(upperBoundInclusive && x > upperBound) return false;
        if(!upperBoundInclusive && x >= upperBound) return false;
        return true;
    }

    public static boolean isOutOfRange(
            double x,
            double lowerBound, boolean lowerBoundInclusive,
            double upperBound, boolean upperBoundInclusive) {
        return !isInRange(x, lowerBound, lowerBoundInclusive, upperBound, upperBoundInclusive);
    }

    public boolean isInRange(double x) {
        return isInRange(x, getLowerBound(), isLowerBoundInclusive(), getUpperBound(), isUpperBoundInclusive());
    }

    public boolean isOutOfRange(double x) {
        return !isInRange(x);
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }
}
