package de.unileipzig.irpact.core.preference;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ValueMapping<T> {

    private T object;
    private Value value;
    private double strength;

    public ValueMapping(T object, Value value, double strength) {
        this.object = object;
        this.value = value;
        this.strength = strength;
    }

    public T getObject() {
        return object;
    }

    public Value getValue() {
        return value;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, value, strength);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof ValueMapping) {
            ValueMapping<?> other = (ValueMapping<?>) obj;
            return strength == other.strength
                    && Objects.equals(value, other.value)
                    && Objects.equals(object, other.object);
        }
        return false;
    }
}
