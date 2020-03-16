package de.unileipzig.irpact.core.affinity;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class BasicUnaryAffinityMapping<A> implements UnaryAffinityMapping<A> {

    private Map<A, Map<A, Double>> mapping;

    public BasicUnaryAffinityMapping(Map<A, Map<A, Double>> mapping) {
        this.mapping = mapping;
    }

    @Override
    public double getValue(A from, A to) {
        Map<A, Double> submapping = mapping.get(from);
        if(submapping == null) throw new NoSuchElementException();
        Double value = submapping.get(to);
        if(value == null) throw new NoSuchElementException();
        return value;
    }

    @Override
    public double getReverseValue(A from, A to) {
        return getValue(from, to);
    }

    @Override
    public double getValue(A from, A to, double defaultValue) {
        Map<A, Double> submapping = mapping.get(from);
        if(submapping == null) return defaultValue;
        Double value = submapping.get(to);
        return value == null
                ? defaultValue
                : value;
    }

    @Override
    public double getReverseValue(A from, A to, double defaultValue) {
        return getValue(from, to, defaultValue);
    }
}
