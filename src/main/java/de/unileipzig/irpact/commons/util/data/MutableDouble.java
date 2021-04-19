package de.unileipzig.irpact.commons.util.data;

import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class MutableDouble extends Number {

    protected final Consumer<?> INC_CONSUMER = obj -> inc();
    protected double value;

    public MutableDouble(double value) {
        set(value);
    }

    public static MutableDouble zero() {
        return new MutableDouble(0.0);
    }

    public static MutableDouble one() {
        return new MutableDouble(1.0);
    }

    public static MutableDouble maxValue() {
        return new MutableDouble(Double.MAX_VALUE);
    }

    public static MutableDouble minValue() {
        return new MutableDouble(Double.MIN_VALUE);
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
    }

    public double get() {
        return value;
    }

    public void add(double delta) {
        value += delta;
    }

    public void sub(double delta) {
        value -= delta;
    }

    public void inc() {
        value++;
    }

    public synchronized void syncUpdate(double delta) {
        value += delta;
    }

    @SuppressWarnings("unchecked")
    public <T> Consumer<T> incConsumer() {
        return (Consumer<T>) INC_CONSUMER;
    }

    public void dec() {
        value--;
    }

    public boolean isEquals(double value) {
        return this.value == value;
    }

    public boolean isZero() {
        return isEquals(0.0);
    }

    public boolean setMax(double value) {
        if(value > get()) {
            set(value);
            return true;
        } else {
            return false;
        }
    }

    public boolean setMin(double value) {
        if(value < get()) {
            set(value);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
