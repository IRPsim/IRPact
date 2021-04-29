package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.function.Supplier;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class NavigableMapWeightedMapping<T> implements WeightedMapping<T> {

    protected Supplier<? extends NavigableMap<Double, T>> mapSupplier;
    protected Map<T, Double> weightMap = new LinkedHashMap<>();
    protected NavigableMap<Double, T> mapping;
    protected double totalWeight = 0;
    protected boolean disableWeights = false;

    public NavigableMapWeightedMapping() {
        this(TreeMap::new);
    }

    public NavigableMapWeightedMapping(Supplier<? extends NavigableMap<Double, T>> mapSupplier) {
        this.mapSupplier = mapSupplier;
        this.mapping = mapSupplier.get();
    }

    public boolean isDisableWeights() {
        return disableWeights;
    }

    public void setDisableWeights(boolean disableWeights) {
        this.disableWeights = disableWeights;
    }

    @Override
    public NavigableMapWeightedMapping<T> copy() {
        Map<T, Double> copy = new LinkedHashMap<>(weightMap);
        return createCopy(copy);
    }

    @Override
    public NavigableMapWeightedMapping<T> copyWithout(T toRemove) {
        Map<T, Double> copy = new LinkedHashMap<>(weightMap);
        copy.remove(toRemove);
        return createCopy(copy);
    }

    protected NavigableMapWeightedMapping<T> createCopy(Map<T, Double> map) {
        NavigableMapWeightedMapping<T> copy = new NavigableMapWeightedMapping<>(mapSupplier);
        copy.weightMap = map;
        copy.rebuildMapping();
        return copy;
    }

    @Override
    public Collection<T> elements() {
        return mapping.values();
    }

    @Override
    public boolean isEmpty() {
        return mapping.isEmpty();
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
        return weightMap.containsKey(target);
    }

    @Override
    public double getWeight(T target) {
        if(has(target)) {
            return weightMap.get(target);
        } else {
            return 0;
        }
    }

    @Override
    public boolean remove(T target) {
        if(has(target)) {
            weightMap.remove(target);
            rebuildMapping();
            return true;
        } else {
            return false;
        }
    }

    protected void rebuildMapping() {
        totalWeight = 0;
        mapping.clear();
        for(Map.Entry<T, Double> entry: weightMap.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public double totalWeight() {
        return totalWeight;
    }

    protected static void nonZeroWeight(double weight) {
        if(weight == 0.0) {
            throw new IllegalArgumentException("weight is zero");
        }
    }

    @Override
    public void set(T target, double weight) {
        nonZeroWeight(weight);
        weightMap.put(target, weight);
        totalWeight += weight;
        mapping.put(totalWeight, target);
    }

    @Override
    public void set(WeightedValue<T> value) {
        set(value.getValue(), value.getWeight());
    }

    protected void checkNotEmpty() {
        if(isEmpty()) {
            throw new IllegalStateException("empty");
        }
    }

    @Override
    public T getRandom(Rnd rnd) {
        checkNotEmpty();
        return rnd.getRandomValue(mapping);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        if(isDisableWeights()) {
            return getRandom(rnd);
        }
        checkNotEmpty();
        double rndDraw = rnd.nextDouble(totalWeight());
        return higherValueOrLast(rndDraw);
    }

    protected T higherValueOrLast(double key) {
        Map.Entry<Double, T> drawn = mapping.higherEntry(key);
        return drawn == null
                ? mapping.lastEntry().getValue()
                : drawn.getValue();
    }
}
