package de.unileipzig.irpact.v2.commons.awareness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class ThresholdAwareness<T> implements Awareness<T> {

    protected Map<T, Double> items;
    protected double threshold;

    public ThresholdAwareness() {
        this(new HashMap<>());
    }

    public ThresholdAwareness(Map<T, Double> items) {
        this.items = items;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    @Override
    public boolean isAwareOf(T item) {
        Double v = items.get(item);
        return v != null && v >= threshold;
    }

    @Override
    public void update(T item, double influence) {
        double current = items.computeIfAbsent(item, _item -> 0.0);
        items.put(item, Math.max(current + influence, 0.0));
    }

    @Override
    public void forget(T item) {
        items.remove(item);
    }
}
