package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractBoundedUnivariateDoubleDistribution extends NameableBase implements BoundedUnivariateDoubleDistribution {

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
