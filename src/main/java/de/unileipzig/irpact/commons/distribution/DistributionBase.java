package de.unileipzig.irpact.commons.distribution;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public abstract class DistributionBase implements Distribution {

    protected String name;

    public DistributionBase(String name) {
        this.name = name;
    }

    protected static Random newRandom(long seed) {
        return seed == NO_SEED
                ? new Random()
                : new Random(seed);
    }

    @Override
    public String getName() {
        return name;
    }
}
