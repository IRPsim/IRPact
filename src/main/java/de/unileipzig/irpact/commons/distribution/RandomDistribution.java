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

    public RandomDistribution(String name, Random rnd) {
        super(name);
        this.rnd = Check.requireNonNull(rnd, "rnd");
        seed = NO_SEED;
    }

    public RandomDistribution(String name, long seed) {
        super(name);
        this.seed = seed;
        this.rnd = newRandom(seed);
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double drawValue() {
        return rnd.nextDouble();
    }
}
