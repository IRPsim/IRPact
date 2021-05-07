package de.unileipzig.irpact.commons.util.data;

import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class MutableInt extends Number {

    protected final Consumer<?> INC_CONSUMER = obj -> inc();
    protected int value;

    public MutableInt() {
    }

    public MutableInt(int value) {
        set(value);
    }

    public static MutableInt zero() {
        return new MutableInt(0);
    }

    public static MutableInt one() {
        return new MutableInt(1);
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
    }

    public int get() {
        return value;
    }

    public void add(int delta) {
        value += delta;
    }

    public void sub(int delta) {
        value -= delta;
    }

    public void inc() {
        value++;
    }

    public synchronized void syncUpdate(int delta) {
        value += delta;
    }

    @SuppressWarnings("unchecked")
    public <T> Consumer<T> incConsumer() {
        return (Consumer<T>) INC_CONSUMER;
    }

    public void dec() {
        value--;
    }

    public boolean isEquals(int value) {
        return this.value == value;
    }

    public boolean isZero() {
        return isEquals(0);
    }

    public boolean setMax(int value) {
        if(value > get()) {
            set(value);
            return true;
        } else {
            return false;
        }
    }

    public boolean setMin(int value) {
        if(value < get()) {
            set(value);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
