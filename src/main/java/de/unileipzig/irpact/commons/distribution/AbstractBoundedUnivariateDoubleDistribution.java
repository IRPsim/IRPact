package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.DoubleRange;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractBoundedUnivariateDoubleDistribution extends NameableBase implements BoundedUnivariateDoubleDistribution {

    protected double lowerBound;
    protected double upperBound;

    protected boolean lowerBoundInclusive = true;
    protected boolean upperBoundInclusive = false;

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    protected boolean isInRange(double drawnValue) {
        return DoubleRange.isInRange(drawnValue, getLowerBound(), isLowerBoundInclusive(), getUpperBound(), isUpperBoundInclusive());
    }

    protected boolean isOutOfRange(double drawnValue) {
        return DoubleRange.isOutOfRange(drawnValue, getLowerBound(), isLowerBoundInclusive(), getUpperBound(), isUpperBoundInclusive());
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    @Override
    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    @Override
    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }
}
