package de.unileipzig.irpact.commons;

import java.util.Random;

/**
 * Wrapper for {@link Random} for simple serialisation.
 *
 * @author Daniel Abitz
 */
public final class Rnd implements IsEquals {

    private static final class Holder {
        private static final Random R = new Random();
    }
    private static synchronized long randomSeed() {
        return Holder.R.nextLong();
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

    private Rnd(Void v) {
        rnd = null;
    }

    /**
     * Creates an instance without a random.
     *
     * @return created instance
     */
    public static Rnd empty() {
        return new Rnd(null);
    }

    public void setInitialSeed(long initialSeed) {
        this.initialSeed = initialSeed;
        this.rnd = new Random(initialSeed);
    }

    /**
     * Creates a new random seed based on {@link #nextLong()} und calls {@link Random#setSeed(long)}.
     *
     * @return new seed
     */
    public long reseed() {
        long nextSeed = rnd.nextLong();
        rnd.setSeed(nextSeed);
        initialSeed = nextSeed;
        return nextSeed;
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public Random getRandom() {
        return rnd;
    }

    public boolean nextBoolean() {
        return rnd.nextBoolean();
    }

    public int nextInt() {
        return rnd.nextInt();
    }

    public long nextLong() {
        return rnd.nextLong();
    }

    public synchronized long syncNextLong() {
        return nextLong();
    }

    public double nextDouble() {
        return rnd.nextDouble();
    }

    public Rnd deriveInstance() {
        long seed = syncNextLong();
        return new Rnd(seed);
    }

    @Override
    public int getHashCode() {
        return Long.hashCode(initialSeed);
    }
}
