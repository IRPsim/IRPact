package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper for {@link Random} for simple serialisation.
 *
 * @author Daniel Abitz
 */
public final class Rnd implements ChecksumComparable {

    private static final Random GLOBAL_RND = new Random();

    public static final long USE_RANDOM_SEED = -1L;

    protected final Lock LOCK = new ReentrantLock();
    protected boolean useLock = false;
    protected long initialSeed;
    protected Random rnd;

    /**
     * Creates an instance with a random initial seed.
     */
    public Rnd() {
        this(nextLongGlobal());
    }

    public Rnd(long initialSeed) {
        setInitialSeed(initialSeed);
    }

    private Rnd(Void v) {
        rnd = null;
    }

    public static synchronized long nextLongGlobal() {
        return GLOBAL_RND.nextLong();
    }

    public static long nextLongGlobalIfEquals(long input, long checkValue) {
        return input == checkValue
                ? nextLongGlobal()
                : input;
    }

    /**
     * Creates an instance without a random.
     *
     * @return created instance
     */
    public static Rnd empty() {
        return new Rnd(null);
    }

    protected void lock() {
        if(useLock) {
            LOCK.lock();;
        }
    }

    protected void unlock() {
        if(useLock) {
            LOCK.unlock();
        }
    }

    public void enableSync() {
        useLock = true;
    }

    public void disableSync() {
        useLock = false;
    }

    public void setInitialSeed(long initialSeed) {
        lock();
        try {
            this.initialSeed = initialSeed;
            rnd = new Random(initialSeed);
        } finally {
            unlock();
        }
    }

    /**
     * Creates a new random seed based on {@link #nextLong()} und calls {@link Random#setSeed(long)}.
     *
     * @return new seed
     */
    public long reseed() {
        lock();
        try {
            long nextSeed = rnd.nextLong();
            rnd.setSeed(nextSeed);
            initialSeed = nextSeed;
            return nextSeed;
        } finally {
            unlock();
        }
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public Random getRandom() {
        return rnd;
    }

    public boolean nextBoolean() {
        lock();
        try {
            return rnd.nextBoolean();
        } finally {
            unlock();
        }
    }

    public int nextInt() {
        lock();
        try {
            return rnd.nextInt();
        } finally {
            unlock();
        }
    }

    public int nextInt(int bound) {
        lock();
        try {
            return rnd.nextInt(bound);
        } finally {
            unlock();
        }
    }

    public int nextInt(int lowerBound, int upperBound) {
        return nextInt(upperBound - lowerBound) + lowerBound;
    }

    public long nextLong() {
        lock();
        try {
            return rnd.nextLong();
        } finally {
            unlock();
        }
    }

    public double nextDouble() {
        lock();
        try {
            return rnd.nextDouble();
        } finally {
            unlock();
        }
    }

    public double nextDouble(double bound) {
        return nextDouble() * bound;
    }

    public double nextDouble(double lowerBound, double upperBound) {
        return (nextDouble() * (upperBound - lowerBound)) + lowerBound;
    }

    public <T> T getRandom(Collection<? extends T> coll) {
        lock();
        try {
            return CollectionUtil.getRandom(coll, this);
        } finally {
            unlock();
        }
    }

    public <T> T removeRandom(Collection<? extends T> coll) {
        lock();
        try {
            return CollectionUtil.removeRandom(coll, this);
        } finally {
            unlock();
        }
    }

    public void shuffle(List<?> coll) {
        lock();
        try {
            Collections.shuffle(coll, rnd);
        } finally {
            unlock();
        }
    }

    public synchronized Rnd deriveInstance() {
        long seed = nextLong();
        return new Rnd(seed);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(initialSeed);
    }
}
