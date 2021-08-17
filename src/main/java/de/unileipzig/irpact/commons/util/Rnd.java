package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.*;
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
        this(false);
    }

    public Rnd(boolean sync) {
        this(nextLongGlobal(), sync);
    }

    public Rnd(long initialSeed) {
        this(initialSeed, false);
    }

    public Rnd(long initialSeed, boolean sync) {
        setInitialSeed(initialSeed);
        if(sync) {
            enableSync();
        } else {
            disableSync();
        }
    }

    public static synchronized long nextLongGlobal() {
        return GLOBAL_RND.nextLong();
    }

    public static long nextLongGlobalIfEquals(long input, long checkValue) {
        return input == checkValue
                ? nextLongGlobal()
                : input;
    }

    protected void lock() {
        if(useLock) {
            LOCK.lock();
        }
    }

    protected void unlock() {
        if(useLock) {
            LOCK.unlock();
        }
    }

    public String printInfo() {
        return "Rnd{" + initialSeed + "}";
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

    public void nextBytes(byte[] bytes) {
        lock();
        try {
            rnd.nextBytes(bytes);
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

    public float nextFloat() {
        lock();
        try {
            return rnd.nextFloat();
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

    public double nextGaussian() {
        return nextGaussian(1.0, 0);
    }

    public double nextGaussian(double standardDeviation, double mean) {
        lock();
        try {
            return rnd.nextGaussian() * standardDeviation + mean;
        } finally {
            unlock();
        }
    }

    public <T> T getRandom(Collection<? extends T> coll) {
        lock();
        try {
            return CollectionUtil.getRandom(coll, this);
        } finally {
            unlock();
        }
    }

    public <K> K getRandomKey(Map<? extends K, ?> map) {
        return getRandom(map.keySet());
    }

    public <V> V getRandomValue(Map<?, ? extends V> map) {
        return getRandom(map.values());
    }

    public <K, V> Map.Entry<K, V> getRandomEntry(Map<K, V> map) {
        return getRandom(map.entrySet());
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
        return Checksums.SMART.getChecksum(initialSeed);
    }

    @Override
    public String toString() {
        return printInfo();
    }
}
