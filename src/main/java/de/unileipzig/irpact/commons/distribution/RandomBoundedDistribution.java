package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.v2.commons.Check;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedDistribution extends UnivariateDistributionBase implements BoundedUnivariateDistribution {

    public static final String NAME = RandomBoundedDistribution.class.getSimpleName();

    private Random rnd;
    private long seed;
    private double lowerBound;
    private double upperBound;
    private double boundDiff;

    public RandomBoundedDistribution(String name, Random rnd) {
        this(name, rnd, 0.0, 1.0);
    }

    public RandomBoundedDistribution(String name, Random rnd, double lowerBound, double upperBound) {
        super(name);
        this.rnd = Check.requireNonNull(rnd, "rnd");
        this.lowerBound = Check.checkFirstSmallerEquals(lowerBound, upperBound, "lowerBound > upperBound: " + lowerBound + " > " + upperBound);
        this.upperBound = Check.checkFirstLargerEquals(upperBound, lowerBound, "upperBound < lowerBound: " + upperBound + " < " + lowerBound); //check ueberfluessing, aber egal
        boundDiff = upperBound - lowerBound;
        seed = NO_SEED;
    }

    public RandomBoundedDistribution(String name, long seed, double lowerBound, double upperBound) {
        super(name);
        this.seed = seed;
        this.rnd = newRandom(seed);
        this.lowerBound = Check.checkFirstSmallerEquals(lowerBound, upperBound, "lowerBound > upperBound: " + lowerBound + " > " + upperBound);
        this.upperBound = Check.checkFirstLargerEquals(upperBound, lowerBound, "upperBound < lowerBound: " + upperBound + " < " + lowerBound); //check ueberfluessing, aber egal
        boundDiff = upperBound - lowerBound;
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double drawValue() {
        return boundDiff * rnd.nextDouble() + lowerBound;
    }
}
