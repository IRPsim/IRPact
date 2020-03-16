package de.unileipzig.irpact.core.preference;

/**
 * @author Daniel Abitz
 */
public class ValueMapping<T> {

    private T object;
    private Value value;
    private double mappingStrength;

    public ValueMapping(T object, Value value, double mappingStrength) {
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
}
