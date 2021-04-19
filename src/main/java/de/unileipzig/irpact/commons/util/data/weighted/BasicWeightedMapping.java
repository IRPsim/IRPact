package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.WeightedValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class BasicWeightedMapping<T> implements WeightedMapping<T> {

    protected Map<T, Double> mapping;
    protected boolean normalized = false;
    protected boolean autoNormalize = false;

    public BasicWeightedMapping() {
        this(new LinkedHashMap<>());
    }

    public BasicWeightedMapping(Map<T, Double> mapping) {
        this.mapping = mapping;
    }

    public void setAutoNormalize(boolean autoNormalize) {
        this.autoNormalize = autoNormalize;
    }

    public boolean isAutoNormalize() {
        return autoNormalize;
    }

    public void clear() {
        mapping.clear();
    }

    public void normalize() {
        if(normalized) {
            return;
        }
        double totalWeight = totalWeight();
        Map<T, Double> normed = new LinkedHashMap<>();
        for(Map.Entry<T, Double> entry: mapping.entrySet()) {
            normed.put(entry.getKey(), entry.getValue() / totalWeight);
        }
        mapping.clear();
        mapping.putAll(normed);
        normed.clear();
        normalized = true;
    }

    protected void tryNormalize() {
        if(autoNormalize) {
            normalize();
        }
    }

    public boolean isNormalized() {
        return normalized;
    }

    protected double totalWeight() {
        return mapping.values()
                .stream()
                .mapToDouble(v -> v)
                .sum();
    }

    public int size() {
        return mapping.size();
    }

    @Override
    public boolean has(T target) {
        return mapping.containsKey(target);
    }

    @Override
    public double getWeight(T target) {
        Double w = mapping.get(target);
        return w == null ? 0.0 : w;
    }

    @Override
    public boolean remove(T target) {
        return mapping.remove(target) != null;
    }

    @Override
    public void set(T target, double weight) {
        mapping.put(target, weight);
        normalized = false;
    }

    @Override
    public void set(WeightedValue<T> value) {
        set(value.getValue(), value.getWeight());
    }

    protected void requriedNotEmpty() {
        if(mapping.isEmpty()) {
            throw new IllegalStateException("empty");
        }
    }

    @Override
    public T getRandom(Rnd rnd) {
        requriedNotEmpty();
        tryNormalize();
        return rnd.getRandomKey(mapping);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        requriedNotEmpty();
        tryNormalize();
        return isNormalized()
                ? getNormalizedWeightedRandom(rnd)
                : getNotNormalizedWeightedRandom(rnd);
    }

    protected T getNormalizedWeightedRandom(Rnd rnd) {
        return getNormalizedWeightedRandom(rnd, 1.0);
    }

    protected T getNotNormalizedWeightedRandom(Rnd rnd) {
        double totalWeight = totalWeight();
        return getNormalizedWeightedRandom(rnd, totalWeight);
    }

    protected T getNormalizedWeightedRandom(Rnd rnd, double totalWeight) {
        return getNormalizedWeightedRandom(mapping, rnd, totalWeight);
    }

    protected static <K> K getNormalizedWeightedRandom(Map<K, Double> mapping, Rnd rnd, double totalWeight) {
        final double rndDraw = rnd.nextDouble(totalWeight);
        double temp = 0.0;
        K draw = null;
        for(Map.Entry<K, Double> entry: mapping.entrySet()) {
            temp += entry.getValue();
            draw = entry.getKey();
            if(rndDraw < temp) {
                return draw;
            }
        }
        return draw;
    }
}
