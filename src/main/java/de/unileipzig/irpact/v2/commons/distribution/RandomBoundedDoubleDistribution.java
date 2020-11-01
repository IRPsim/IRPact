package de.unileipzig.irpact.v2.commons.distribution;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedDoubleDistribution extends AbstractBoundedUnivariateDoubleDistribution implements BoundedUnivariateDoubleDistribution {

    protected long seed;
    protected Random rnd;

    public RandomBoundedDoubleDistribution() {
    }

    public RandomBoundedDoubleDistribution(String name, double lowerBound, double upperBound, long seed) {
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
    public double drawDoubleValue() {
        return rnd.nextDouble() * (upperBound - lowerBound) + lowerBound;
    }
}
