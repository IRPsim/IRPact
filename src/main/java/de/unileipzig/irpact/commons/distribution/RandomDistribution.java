package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.Check;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class RandomDistribution extends UnivariateDistributionBase {

    public static final String NAME = RandomDistribution.class.getSimpleName();

    private Random rnd;
    private long seed;
    private double lowerBound;
    private double upperBound;
    private double temp;

    public RandomDistribution(String name, Random rnd) {
        this(name, rnd, 0.0, 1.0);
    }

    public RandomDistribution(String name, Random rnd, double lowerBound, double upperBound) {
        super(name);
        this.rnd = Check.requireNonNull(rnd, "rnd");
        this.lowerBound = Check.checkFirstSmallerEquals(lowerBound, upperBound, "lowerBound > upperBound: " + lowerBound + " > " + upperBound);
        this.upperBound = Check.checkFirstLargerEquals(upperBound, lowerBound, "upperBound < lowerBound: " + upperBound + " < " + lowerBound); //check ueberfluessing, aber egal
        temp = upperBound - lowerBound;
        seed = NO_SEED;
    }

    public RandomDistribution(String name, long seed, double lowerBound, double upperBound) {
        super(name);
        this.seed = seed;
        this.rnd = newRandom(seed);
        this.lowerBound = Check.checkFirstSmallerEquals(lowerBound, upperBound, "lowerBound > upperBound: " + lowerBound + " > " + upperBound);
        this.upperBound = Check.checkFirstLargerEquals(upperBound, lowerBound, "upperBound < lowerBound: " + upperBound + " < " + lowerBound); //check ueberfluessing, aber egal
        temp = upperBound - lowerBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double drawValue() {
        return temp * rnd.nextDouble() + lowerBound;
    }
}
