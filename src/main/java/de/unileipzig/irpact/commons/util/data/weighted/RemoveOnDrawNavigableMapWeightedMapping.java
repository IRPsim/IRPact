package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.Mutable;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.develop.XXXXXXXXX;

import java.util.*;
import java.util.function.Supplier;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class RemoveOnDrawNavigableMapWeightedMapping<T> implements WeightedMapping<T> {

    protected Supplier<? extends NavigableMap<Double, T>> mapSupplier;
    protected Map<T, Double> weightMap = new LinkedHashMap<>();
    protected NavigableMap<Double, T> mapping;
    protected double totalWeight = 0;
    protected boolean disableWeights = false;

    public RemoveOnDrawNavigableMapWeightedMapping() {
        this(TreeMap::new);
    }

    public RemoveOnDrawNavigableMapWeightedMapping(Supplier<? extends NavigableMap<Double, T>> mapSupplier) {
        this.mapSupplier = mapSupplier;
        this.mapping = mapSupplier.get();
    }

    public boolean isDisableWeights() {
        return disableWeights;
    }

    public void setDisableWeights(boolean disableWeights) {
        this.disableWeights = disableWeights;
    }

    public void normalizeThis() {
        final double totalWeight = totalWeight();
        weightMap.replaceAll((element, weight) -> weight / totalWeight);
        rebuildMapping();
    }

    @Override
    public RemoveOnDrawNavigableMapWeightedMapping<T> copy() {
        Map<T, Double> copy = new LinkedHashMap<>(weightMap);
        return createCopy(copy);
    }

    @Override
    public RemoveOnDrawNavigableMapWeightedMapping<T> copyWithout(T toRemove) {
        Map<T, Double> copy = new LinkedHashMap<>(weightMap);
        copy.remove(toRemove);
        return createCopy(copy);
    }

    protected RemoveOnDrawNavigableMapWeightedMapping<T> createCopy(Map<T, Double> map) {
        RemoveOnDrawNavigableMapWeightedMapping<T> copy = new RemoveOnDrawNavigableMapWeightedMapping<>(mapSupplier);
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

    protected double getSummedWeight(T target) {
        double summendWeight = Double.NaN;
        for(Map.Entry<Double, T> entry: mapping.entrySet()) {
            if(Objects.equals(target, entry.getValue())) {
                summendWeight = entry.getKey();
                break;
            }
        }
        if(Double.isNaN(summendWeight)) {
            throw new NoSuchElementException();
        }
        return summendWeight;
    }

    @Override
    public boolean remove(T target) {
        if(has(target)) {
            double summendWeight = getSummedWeight(target);
            //Hier ist inklusive true, da summendWeight der exakte Wert fuer T ist.
            T removed = removeAndUpdate(summendWeight, true);
            //sanity check
            if(target != removed) {
                throw new IllegalStateException();
            }
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        totalWeight = 0;
        mapping.clear();
        weightMap.clear();
    }

    protected void rebuildMapping() {
        totalWeight = 0;
        mapping.clear();
        for(Map.Entry<T, Double> entry: weightMap.entrySet()) {
            set(entry.getKey(), entry.getValue(), true);
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
        set(target, weight, false);
    }

    protected void set(T target, double weight, boolean rebuild) {
        nonZeroWeight(weight);
        totalWeight += weight;
        mapping.put(totalWeight, target);
        if(!rebuild) {
            weightMap.put(target, weight);
        }
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
        Map.Entry<Double, T> toRemove = rnd.getRandomEntry(mapping);
        T toRemoveValue = toRemove.getValue();
        T removedValue = removeAndUpdate(toRemove.getKey(), true);
        //sanity check
        if(removedValue != toRemoveValue) {
            throw new IllegalStateException(removedValue + " != " + toRemoveValue);
        }
        return removedValue;
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        if(isDisableWeights()) {
            return getRandom(rnd);
        }

        checkNotEmpty();

        if(mapping.size() == 1) {
            T removed = mapping.pollFirstEntry().getValue();
            weightMap.clear();
            totalWeight = 0;
            return removed;
        } else {
            double rndDraw = rnd.nextDouble(totalWeight());
            //Hier ist inclusive false, weil die Obergrenze bei nextDouble exklusiv ist.
            return removeAndUpdate(rndDraw, false);
        }
    }

    protected T removeAndUpdate(double key, boolean inclusive) {
        NavigableMap<Double, T> tail = mapping.tailMap(key, inclusive);
        T removed = tail.pollFirstEntry().getValue();
        double weight = weightMap.remove(removed);
        totalWeight -= weight;

        Map<Double, T> temp = new HashMap<>();
        for(Map.Entry<Double, T> entry: tail.entrySet()) {
            temp.put(entry.getKey() - weight, entry.getValue());
        }
        tail.clear();
        mapping.putAll(temp);
        temp.clear();

        return removed;
    }
}
