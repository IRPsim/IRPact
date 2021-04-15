package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class ThresholdInterest<T, U> implements Interest<T> {

    protected Map<T, Double> items;
    protected Map<U, Double> thresholds;

    public ThresholdInterest() {
        this(CollectionUtil.newMap(), CollectionUtil.newMap());
    }

    public ThresholdInterest(Map<T, Double> items, Map<U, Double> thresholds) {
        this.items = items;
        this.thresholds = thresholds;
    }

    public void setThreshold(U key, double threshold) {
        thresholds.put(key, threshold);
    }

    public double getThreshold(U key) {
        Double threshold = thresholds.get(key);
        return threshold == null
                ? 0
                : threshold;
    }

    protected abstract double getThresholdFor(T item);

    public Map<T, Double> getItems() {
        return items;
    }

    public Map<U, Double> getThresholds() {
        return thresholds;
    }

    @Override
    public boolean isInterested(T item) {
        Double v = items.get(item);
        return v != null && v >= getThresholdFor(item);
    }

    @Override
    public void update(T item, double influence) {
        double current = items.computeIfAbsent(item, _item -> 0.0);
        items.put(item, Math.max(current + influence, 0.0));
    }

    @Override
    public void makeInterested(T item) {
        items.put(item, getThresholdFor(item));
    }

    @Override
    public void forget(T item) {
        items.remove(item);
    }

    @Override
    public double getValue(T item) {
        Double value = items.get(item);
        return value == null
                ? 0
                : value;
    }
}
