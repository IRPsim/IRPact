package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.v2.commons.Check;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class BooleanDistribution extends UnivariateDistributionBase {

    public static final String NAME = BooleanDistribution.class.getSimpleName();

    private Random rnd;
    private long seed;
    private double threshold;

    public BooleanDistribution(String name, Random rnd, double threshold) {
        super(name);
        this.rnd = Check.requireNonNull(rnd, "rnd");
        this.threshold = Check.require0to1(threshold, "threshold");
        seed = NO_SEED;
    }

    public BooleanDistribution(String name, long seed, double threshold) {
        super(name);
        this.seed = seed;
        this.rnd = newRandom(seed);
        this.threshold = Check.require0to1(threshold, "threshold");
    }

    public double getThreshold() {
        return threshold;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double drawValue() {
        return rnd.nextDouble() < threshold
                ? 1.0
                : 0.0;
    }
}
