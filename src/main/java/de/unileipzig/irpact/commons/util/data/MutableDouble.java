package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Daniel Abitz
 */
public final class MutableDouble extends Number implements ChecksumComparable {

    protected final Consumer<?> INC_CONSUMER = obj -> inc();
    protected boolean hasValue;
    protected double value;

    public MutableDouble() {
        clear();
    }

    public MutableDouble(double value) {
        set(value);
    }

    public static MutableDouble empty() {
        return new MutableDouble();
    }

    public static MutableDouble zero() {
        return new MutableDouble(0);
    }

    public static MutableDouble one() {
        return new MutableDouble(1);
    }

    public static MutableDouble NaN() {
        return new MutableDouble(Double.NaN);
    }

    public static MutableDouble negativeMaxValue() {
        return new MutableDouble(-Double.MAX_VALUE);
    }

    public static MutableDouble positiveMaxValue() {
        return new MutableDouble(Double.MAX_VALUE);
    }

    public static MutableDouble wrap(double value) {
        return new MutableDouble(value);
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
        set(Double.MAX_VALUE);
    }

    public void setMinValue() {
        set(Double.MIN_VALUE);
    }

    public void set(double value) {
        this.value = value;
        this.hasValue = true;
    }

    public double get() {
        requiresValue();
        return value;
    }

    public double getAndSet(double newValue) {
        double current = get();
        set(newValue);
        return current;
    }

    public void add(double delta) {
        requiresValue();
        value += delta;
    }

    public void sub(double delta) {
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

    public synchronized void syncUpdate(double delta) {
        requiresValue();
        value += delta;
    }

    public void modifiy(DoubleUnaryOperator op) {
        if(hasValue()) {
            set(op.applyAsDouble(get()));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Consumer<T> getIncConsumer() {
        return (Consumer<T>) INC_CONSUMER;
    }

    public void dec() {
        requiresValue();
        value--;
    }

    public boolean isEquals(double value) {
        requiresValue();
        return this.value == value;
    }

    public boolean isEquals(MutableDouble other) {
        return isEquals(other.get());
    }

    public boolean isZero() {
        return isEquals(0);
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public boolean isNumber() {
        return !isNaN();
    }

    public boolean setMax(double value) {
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

    public boolean setMin(double value) {
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
        return (long) value;
    }

    @Override
    public float floatValue() {
        requiresValue();
        return (float) value;
    }

    @Override
    public double doubleValue() {
        requiresValue();
        return value;
    }

    @Override
    public String toString() {
        return hasValue()
                ? "MutableDouble(" + value + ")"
                : "MutableDouble.empty";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableDouble)) return false;
        MutableDouble that = (MutableDouble) o;
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
