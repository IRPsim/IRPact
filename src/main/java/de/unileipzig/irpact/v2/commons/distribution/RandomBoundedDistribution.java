package de.unileipzig.irpact.v2.commons.distribution;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedDistribution extends AbstractBoundedUnivariateDistribution implements BoundedUnivariateDistribution {

    protected long seed;
    protected Random rnd;

    public RandomBoundedDistribution() {
    }

    public RandomBoundedDistribution(String name, double lowerBound, double upperBound, long seed) {
        setName(name);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        init(seed);
    }

    public void init(long seed) {
        setSeed(seed);
        setRandom(new Random(seed));
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    public Random getRandom() {
        return rnd;
    }

    @Override
    public double drawValue() {
        return rnd.nextDouble() * (upperBound - lowerBound) + lowerBound;
    }
}
