package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class MapBasedWeightedMapping<T> implements WeightedMapping<T> {

    protected Supplier<? extends Map<T, Double>> mapSupplier;
    protected Map<T, Double> mapping;
    protected boolean normalized = false;
    protected boolean autoNormalize = false;
    protected boolean disableWeights = false;

    public MapBasedWeightedMapping() {
        this(LinkedHashMap::new);
    }

    public MapBasedWeightedMapping(Supplier<? extends Map<T, Double>> mapSupplier) {
        this(mapSupplier, mapSupplier.get());
    }

    public MapBasedWeightedMapping(Supplier<? extends Map<T, Double>> mapSupplier, Map<T, Double> mapping) {
        this.mapSupplier = mapSupplier;
        this.mapping = mapping;
    }

    @Override
    public WeightedMapping<T> copy() {
        Map<T, Double> copy = mapSupplier.get();
        copy.putAll(mapping);
        return createCopy(copy);
    }

    @Override
    public WeightedMapping<T> copyWithout(T toRemove) {
        Map<T, Double> copy = mapSupplier.get();
        copy.putAll(mapping);
        copy.remove(toRemove);
        return createCopy(copy);
    }

    protected WeightedMapping<T> createCopy(Map<T, Double> copy) {
        MapBasedWeightedMapping<T> copyMapping = new MapBasedWeightedMapping<>(mapSupplier, copy);
        copyMapping.setDisableWeights(isDisableWeights());
        copyMapping.setAutoNormalize(isAutoNormalize());
        return copyMapping;
    }

    @Override
    public Collection<T> elements() {
        return mapping.keySet();
    }

    public void setAutoNormalize(boolean autoNormalize) {
        this.autoNormalize = autoNormalize;
    }

    public boolean isAutoNormalize() {
        return autoNormalize;
    }

    public boolean isDisableWeights() {
        return disableWeights;
    }

    public void setDisableWeights(boolean disableWeights) {
        this.disableWeights = disableWeights;
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

    @Override
    public double totalWeight() {
        return mapping.values()
                .stream()
                .mapToDouble(v -> v)
                .sum();
    }

    @Override
    public int size() {
        return mapping.size();
    }

    @Override
    public boolean allowsZeroWeight() {
        return false;
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

    protected static void nonZeroWeight(double weight) {
        if(weight == 0.0) {
            throw new IllegalArgumentException("weight is zero");
        }
    }

    @Override
    public void set(T target, double weight) {
        nonZeroWeight(weight);
        mapping.put(target, weight);
        normalized = false;
    }

    @Override
    public void set(WeightedValue<T> value) {
        set(value.getValue(), value.getWeight());
    }

    @Override
    public boolean isEmpty() {
        return mapping.isEmpty();
    }

    protected void checkNotEmpty() {
        if(isEmpty()) {
            throw new IllegalStateException("empty");
        }
    }

    @Override
    public T getRandom(Rnd rnd) {
        checkNotEmpty();
        return rnd.getRandomKey(mapping);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        if(disableWeights) {
            return getRandom(rnd);
        }
        checkNotEmpty();
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
        boolean drawn = false;
        for(Map.Entry<K, Double> entry: mapping.entrySet()) {
            double weight = entry.getValue();
            if(weight == 0.0) {
                continue;
            }
            temp += weight;
            draw = entry.getKey();
            drawn = true;
            if(rndDraw < temp) {
                return draw;
            }
        }
        if(!drawn) {
            throw new IllegalStateException();
        }
        return draw;
    }
}
