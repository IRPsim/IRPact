package de.unileipzig.irpact.commons;

import java.util.Random;

/**
 * Wrapper for {@link Random} for simple serialisation.
 *
 * @author Daniel Abitz
 */
public final class Rnd {

    private static final Random R = new Random();
    public static synchronized long randomSeed() {
        return R.nextLong();
    }

    protected long initialSeed;
    protected Random rnd;

    /**
     * Creates an instance with a random initial seed.
     */
    public Rnd() {
        this(randomSeed());
    }

    public Rnd(long initialSeed) {
        setInitialSeed(initialSeed);
    }

    public void setInitialSeed(long initialSeed) {
        this.initialSeed = initialSeed;
        this.rnd = new Random(initialSeed);
    }

    public void reseed() {
        long nextSeed = rnd.nextLong();
        rnd.setSeed(nextSeed);
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public Random getRandom() {
        return rnd;
    }

    public synchronized long syncNextLong() {
        return rnd.nextLong();
    }

    public int nextInt() {
        return rnd.nextInt();
    }

    public double nextDouble() {
        return rnd.nextDouble();
    }

    public Rnd createNewRandom() {
        long seed = syncNextLong();
        return new Rnd(seed);
    }
}
