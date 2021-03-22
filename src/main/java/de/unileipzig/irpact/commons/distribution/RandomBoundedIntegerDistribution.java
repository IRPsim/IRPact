package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.Rnd;

import java.util.Objects;

/**
 * Returns the floor value.
 *
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistribution extends AbstractBoundedUnivariateDoubleDistribution implements BoundedUnivariateDoubleDistribution {

    protected Rnd rnd;

    public RandomBoundedIntegerDistribution() {
    }

    public RandomBoundedIntegerDistribution(String name, double lowerBound, double upperBound, Rnd rnd) {
        setName(name);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setRandom(rnd);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    @Override
    public double drawDoubleValue() {
        return Math.floor(rnd.nextDouble() * (upperBound - lowerBound) + lowerBound);
    }

    @Override
    public int getHashCode() {
        return Objects.hash(name, lowerBound, upperBound, rnd.getHashCode());
    }
}