package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class MutableLong extends Number implements ChecksumComparable {

    protected final Consumer<?> INC_CONSUMER = obj -> inc();
    protected boolean hasValue;
    protected long value;

    public MutableLong() {
        clear();
    }

    public MutableLong(long value) {
        set(value);
    }

    public static MutableLong empty() {
        return new MutableLong();
    }

    public static MutableLong zero() {
        return new MutableLong(0);
    }

    public static MutableLong one() {
        return new MutableLong(1);
    }

    public static MutableLong wrap(long value) {
        return new MutableLong(value);
    }

    protected void requiresValue() {
        if(!hasValue) {
            throw new IllegalStateException("no value");
        }
    }

    public boolean hasValue() {
        return hasValue;
    }

    public boolean isEmpty() {
        return !hasValue();
    }

    public void clear() {
        hasValue = false;
        value = 0;
    }

    public void setZero() {
        set(0);
    }

    public void setOne() {
        set(1);
    }

    public void setMaxValue() {
        set(Long.MAX_VALUE);
    }

    public void setMinValue() {
        set(Long.MIN_VALUE);
    }

    public void set(long value) {
        this.value = value;
        this.hasValue = true;
    }

    public long get() {
        requiresValue();
        return value;
    }

    public long getAndSet(long newValue) {
        long current = get();
        set(newValue);
        return current;
    }

    public void add(long delta) {
        requiresValue();
        value += delta;
    }

    public void sub(long delta) {
        requiresValue();
        value -= delta;
    }

    public void inc() {
        requiresValue();
        value++;
    }

    public void update(long delta) {
        value += delta;
    }

    public synchronized void syncUpdate(long delta) {
        requiresValue();
        value += delta;
    }

    @SuppressWarnings("unchecked")
    public <T> Consumer<T> getIncConsumer() {
        return (Consumer<T>) INC_CONSUMER;
    }

    public void dec() {
        requiresValue();
        value--;
    }

    public boolean isEquals(int value) {
        requiresValue();
        return this.value == value;
    }

    public boolean isZero() {
        return isEquals(0);
    }

    public boolean setMax(long value) {
        if(hasValue()) {
            if(value > get()) {
                set(value);
                return true;
            } else {
                return false;
            }
        } else {
            set(value);
            return true;
        }
    }

    public boolean setMin(long value) {
        if(hasValue()) {
            if(value < get()) {
                set(value);
                return true;
            } else {
                return false;
            }
        } else {
            set(value);
            return true;
        }
    }

    @Override
    public int intValue() {
        requiresValue();
        return (int) value;
    }

    @Override
    public long longValue() {
        requiresValue();
        return value;
    }

    @Override
    public float floatValue() {
        requiresValue();
        return value;
    }

    @Override
    public double doubleValue() {
        requiresValue();
        return value;
    }

    @Override
    public String toString() {
        return hasValue()
                ? "MutableLong(" + value + ")"
                : "MutableLong.empty";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableLong)) return false;
        MutableLong that = (MutableLong) o;
        return value == that.value && hasValue == that.hasValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, hasValue);
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(value, hasValue);
    }
}
