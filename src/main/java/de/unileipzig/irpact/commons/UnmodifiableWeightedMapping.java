package de.unileipzig.irpact.commons;

import java.util.*;
import java.util.function.Supplier;

/**
 * Uses a normalized, sorted list to optimize {@link #getWeightedRandom(Object, Random)}.
 *
 * @author Daniel Abitz
 */
public class UnmodifiableWeightedMapping<S, T, W> implements WeightedMapping<S, T, W> {

    protected Supplier<? extends Map<S, List<WeightedValue<T>>>> mapSupplier;
    protected Supplier<? extends List<WeightedValue<T>>> listSupplier;
    protected Map<S, List<WeightedValue<T>>> normalizedSortedValues;
    protected BasicWeightedMapping<S, T, W> back;
    protected boolean immutable = false;

    public UnmodifiableWeightedMapping() {
        this(new BasicWeightedMapping<>());
    }

    public UnmodifiableWeightedMapping(
            BasicWeightedMapping<S, T, W> back) {
        this(back, LinkedHashMap::new, ArrayList::new);
    }

    public UnmodifiableWeightedMapping(
            BasicWeightedMapping<S, T, W> back,
            Supplier<? extends Map<S, List<WeightedValue<T>>>> mapSupplier,
            Supplier<? extends List<WeightedValue<T>>> listSupplier) {
        this.back = back;
        this.mapSupplier = mapSupplier;
        this.listSupplier = listSupplier;
        normalizedSortedValues = mapSupplier.get();
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void makeImmutable() {
        if(!immutable) {
            immutable = true;
            createSortedValues();
        }
    }

    public void createSortedValues() {
        for(Map.Entry<S, Map<T, W>> mapEntry: back.mapping.entrySet()) {
            List<WeightedValue<T>> list = listSupplier.get();
            S key = mapEntry.getKey();
            double sum = back.sum(key);
            for(Map.Entry<T, W> map0entry: mapEntry.getValue().entrySet()) {
                T value = map0entry.getKey();
                double weight = back.weightFunction.applyAsDouble(map0entry.getValue());
                double normWeight = weight / sum;
                list.add(new WeightedValue<>(value, normWeight));
            }
            list.sort(WeightedValue.getDescendingWeightComparator());
            normalizedSortedValues.put(key, list);
        }
    }

    protected void checkImmutable() {
        if(immutable) {
            throw new IllegalStateException("immutable");
        }
    }

    public void clear() {
        checkImmutable();
        normalizedSortedValues.clear();
        back.clear();
    }

    public Map<S, List<WeightedValue<T>>> getNormalizedSortedValues() {
        return normalizedSortedValues;
    }

    @Override
    public boolean has(S source) {
        return back.has(source);
    }

    @Override
    public boolean has(S source, T target) {
        return back.has(source, target);
    }

    @Override
    public W get(S source, T target) {
        return back.get(source, target);
    }

    @Override
    public W put(S source, T target, W value) {
        checkImmutable();
        return back.put(source, target, value);
    }

    @Override
    public boolean remove(S source) {
        checkImmutable();
        return back.remove(source);
    }

    @Override
    public boolean remove(S source, T target) {
        checkImmutable();
        return back.remove(source, target);
    }

    @Override
    public T getRandom(S source, Random rnd) {
        if(!isImmutable()) {
            return back.getRandom(source, rnd);
        }

        List<WeightedValue<T>> list = normalizedSortedValues.get(source);
        if(list == null || list.isEmpty()) {
            return null;
        }
        if(list.size() == 1) {
            return list.get(0).getValue();
        }

        return CollectionUtil.getRandom(list, rnd)
                .getValue();
    }

    @Override
    public T getWeightedRandom(S source, Random rnd) {
        if(!isImmutable()) {
            return back.getWeightedRandom(source, rnd);
        }

        List<WeightedValue<T>> list = normalizedSortedValues.get(source);
        if(list == null || list.isEmpty()) {
            return null;
        }
        if(list.size() == 1) {
            return list.get(0).getValue();
        }

        final double rndDraw = rnd.nextDouble();
        T draw = null;
        double temp = 0.0;
        for(WeightedValue<T> wv: list) {
            temp += wv.getWeight();
            draw = wv.getValue();
            if(rndDraw < temp) {
                return draw;
            }
        }
        return draw;
    }
}
