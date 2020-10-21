package de.unileipzig.irpact.v2.commons.distribution;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractBoundedUnivariateDistribution extends NameableBase implements BoundedUnivariateDistribution {

    protected double lowerBound;
    protected double upperBound;

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
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
}
