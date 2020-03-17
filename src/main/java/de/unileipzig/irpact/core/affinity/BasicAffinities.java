package de.unileipzig.irpact.core.affinity;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAffinities<T> implements Affinities<T> {

    protected Map<T, Double> values;

    public BasicAffinities(Map<T, Double> values) {
        this.values = values;
    }

    public void putValue(T target, double value) {
        values.put(target, value);
    }

    public void removeValue(T target) {
        values.remove(target);
    }

    @Override
    public boolean hasValue(T target) {
        return values.containsKey(target);
    }

    @Override
    public double getValue(T target) {
        return values.get(target);
    }
}
