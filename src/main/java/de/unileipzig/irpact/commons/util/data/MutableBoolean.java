package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public final class MutableBoolean extends Number {

    protected boolean value;

    public MutableBoolean(boolean value) {
        set(value);
    }

    public static MutableBoolean trueValue() {
        return new MutableBoolean(true);
    }

    public static MutableBoolean falseValue() {
        return new MutableBoolean(false);
    }

    public void set(boolean value) {
        this.value = value;
    }

    public void setTrue() {
        set(true);
    }

    public void setFalse() {
        set(false);
    }

    public boolean get() {
        return value;
    }

    public void or(boolean other) {
        value |= other;
    }

    public void and(boolean other) {
        value &= other;
    }

    public boolean isEquals(boolean value) {
        return this.value == value;
    }

    public boolean isTrue() {
        return value;
    }

    public boolean isFalse() {
        return !value;
    }

    @Override
    public int intValue() {
        return value ? 1 : 0;
    }

    @Override
    public long longValue() {
        return value ? 1 : 0;
    }

    @Override
    public float floatValue() {
        return value ? 1 : 0;
    }

    @Override
    public double doubleValue() {
        return value ? 1 : 0;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
