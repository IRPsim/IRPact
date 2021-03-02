package de.unileipzig.irpact.commons.interest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ThresholdInterest<T> implements Interest<T> {

    protected Map<T, Double> items;
    protected double threshold;

    public ThresholdInterest() {
        this(new LinkedHashMap<>());
    }

    public ThresholdInterest(Map<T, Double> items) {
        this.items = items;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public Map<T, Double> getItems() {
        return items;
    }

    @Override
    public boolean isInterested(T item) {
        Double v = items.get(item);
        return v != null && v >= threshold;
    }

    @Override
    public boolean isAware(T item) {
        Double v = items.get(item);
        return v != null && v > 0.0;
    }

    @Override
    public void update(T item, double influence) {
        if(influence != 0.0) {
            double current = items.computeIfAbsent(item, _item -> 0.0);
            items.put(item, Math.max(current + influence, 0.0));
        }
    }

    @Override
    public void makeInterested(T item) {
        items.put(item, threshold);
    }

    @Override
    public void forget(T item) {
        items.remove(item);
    }
}
