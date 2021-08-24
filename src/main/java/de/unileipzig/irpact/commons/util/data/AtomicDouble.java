package de.unileipzig.irpact.commons.util.data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Daniel Abitz
 */
public final class AtomicDouble {

    private final AtomicLong BITS;

    public AtomicDouble(double value) {
        BITS = new AtomicLong(toLong(value));
    }

    private static long toLong(double value) {
        return Double.doubleToLongBits(value);
    }

    private static double toDouble(long value) {
        return Double.longBitsToDouble(value);
    }

    public AtomicLong getBits() {
        return BITS;
    }

    public double get() {
        return toDouble(BITS.get());
    }

    public void set(double value) {
        BITS.set(toLong(value));
    }

    public boolean compareAndSet(double expectedValue, double newValue) {
        return BITS.compareAndSet(toLong(expectedValue), toLong(newValue));
    }
}
