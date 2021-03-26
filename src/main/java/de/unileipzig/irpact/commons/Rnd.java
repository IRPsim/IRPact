package de.unileipzig.irpact.commons;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    protected final Lock LOCK = new ReentrantLock();
    protected boolean useLock = false;
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

    public long nextLong() {
        lock();
        try {
            return rnd.nextLong();
        } finally {
            unlock();
        }
    }

    public synchronized long syncNextLong() {
        return nextLong();
    }

    public double nextDouble() {
        lock();
        try {
            return rnd.nextDouble();
        } finally {
            unlock();
        }
    }

    public double unsyncNextDouble() {
        return rnd.nextDouble();
    }

    public synchronized double syncNextDouble() {
        return rnd.nextDouble();
    }

    public <T> T getRandom(Collection<? extends T> coll) {
        return CollectionUtil.getRandom(coll, this);
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
