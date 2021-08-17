package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class MutableInt extends Number implements ChecksumComparable {

    protected final Consumer<?> INC_CONSUMER = obj -> inc();
    protected boolean hasValue;
    protected int value;

    public MutableInt() {
        clear();
    }

    public MutableInt(int value) {
        set(value);
    }

    public static MutableInt empty() {
        return new MutableInt();
    }

    public static MutableInt zero() {
        return new MutableInt(0);
    }

    public static MutableInt one() {
        return new MutableInt(1);
    }

    public static MutableInt wrap(int value) {
        return new MutableInt(value);
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
        set(Integer.MAX_VALUE);
    }

    public void setMinValue() {
        set(Integer.MIN_VALUE);
    }

    public void set(int value) {
        this.value = value;
        this.hasValue = true;
    }

    public int get() {
        requiresValue();
        return value;
    }

    public int getAndSet(int newValue) {
        int current = get();
        set(newValue);
        return current;
    }

    public void add(int delta) {
        requiresValue();
        value += delta;
    }

    public void sub(int delta) {
        requiresValue();
        value -= delta;
    }

    public void inc() {
        requiresValue();
        value++;
    }

    public void update(int delta) {
        value += delta;
    }

    public synchronized void syncUpdate(int delta) {
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

    public boolean setMax(int value) {
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

    public boolean setMin(int value) {
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
        return value;
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
                ? "MutableInt(" + value + ")"
                : "MutableInt.empty";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableInt)) return false;
        MutableInt that = (MutableInt) o;
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
