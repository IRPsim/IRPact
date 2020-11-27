package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class BooleanDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected long seed;
    protected Random rnd;

    public BooleanDistribution() {
    }

    public BooleanDistribution(String name, long seed) {
        setName(name);
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
        return rnd.nextBoolean() ? 1.0 : 0.0;
    }
}
