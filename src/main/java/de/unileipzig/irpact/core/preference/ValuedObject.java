package de.unileipzig.irpact.core.preference;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ValuedObject<T> {

    private T object;
    private Value value;
    private double mappingStrength;

    public ValuedObject(T object, Value value, double mappingStrength) {
        this.object = object;
        this.value = value;
        this.mappingStrength = mappingStrength;
    }

    public T getObject() {
        return object;
    }

    public Value getValue() {
        return value;
    }

    public double getMappingStrength() {
        return mappingStrength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, value, mappingStrength);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof ValuedObject) {
            ValuedObject<?> other = (ValuedObject<?>) obj;
            return mappingStrength == other.mappingStrength
                    && Objects.equals(value, other.value)
                    && Objects.equals(object, other.object);
        }
        return false;
    }
}
