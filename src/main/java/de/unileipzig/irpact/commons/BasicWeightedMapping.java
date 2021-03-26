package de.unileipzig.irpact.commons;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

/**
 * @param <S>
 * @param <T>
 * @param <W>
 * @author Daniel Abitz
 */
public class BasicWeightedMapping<S, T, W> implements WeightedMapping<S, T, W> {

    protected static final ToDoubleFunction<? super Object> DEFAULT_W_FUNC = w -> {
        if(w instanceof Number) {
            return ((Number) w).doubleValue();
        } else {
            return 1.0;
        }
    };

    protected Supplier<? extends Map<S, Map<T, W>>> mappingSupplier;
    protected Supplier<? extends Map<T, W>> map0Supplier;
    protected ToDoubleFunction<? super W> weightFunction;
    protected Map<S, Map<T, W>> mapping;

    public BasicWeightedMapping() {
        this(DEFAULT_W_FUNC);
    }

    public BasicWeightedMapping(
            ToDoubleFunction<? super W> weightFunction) {
        this(LinkedHashMap::new, LinkedHashMap::new, weightFunction);
    }

    public BasicWeightedMapping(
            Supplier<? extends Map<S, Map<T, W>>> mappingSupplier,
            Supplier<? extends Map<T, W>> map0Supplier,
            ToDoubleFunction<? super W> weightFunction) {
        this.mappingSupplier = mappingSupplier;
        this.map0Supplier = map0Supplier;
        setWeightFunction(weightFunction);
        mapping = mappingSupplier.get();
    }

    public void setWeightFunction(ToDoubleFunction<? super W> weightFunction) {
        this.weightFunction = weightFunction;
    }

    public void clear() {
        mapping.clear();
    }

    public Map<S, Map<T, W>> getMapping() {
        return mapping;
    }

    @Override
    public boolean has(S source) {
        return mapping.containsKey(source);
    }

    @Override
    public boolean has(S source, T target) {
        Map<T, W> map0 = mapping.get(source);
        if(map0 == null) return false;
        return map0.containsKey(target);
    }

    @Override
    public W get(S source, T target) {
        Map<T, W> map0 = mapping.get(source);
        if(map0 == null) return null;
        return map0.get(target);
    }

    @Override
    public W put(S source, T target, W value) {
        Map<T, W> map0 = mapping.computeIfAbsent(source, _source -> map0Supplier.get());
        return map0.put(target, value);
    }

    @Override
    public boolean remove(S source) {
        return mapping.remove(source) != null;
    }

    @Override
    public boolean remove(S source, T target) {
        Map<T, W> map0 = mapping.get(source);
        if(map0 == null) return false;
        return map0.remove(target) != null;
    }

    @Override
    public T getRandom(S source, Random rnd) {
        Map<T, W> map0 = mapping.get(source);
        return map0 == null || map0.isEmpty()
                ? null
                : CollectionUtil.getRandom(map0.keySet(), rnd);
    }

    public double sum(S source) {
        Map<T, W> map0 = mapping.get(source);
        return map0 == null
                ? 0.0
                : map0.values()
                .stream()
                .mapToDouble(w -> weightFunction.applyAsDouble(w))
                .sum();
    }

    @Override
    public T getWeightedRandom(S source, Random rnd) {
        Map<T, W> map0 = mapping.get(source);
        if(map0 == null || map0.isEmpty()) {
            return null;
        }
        if(map0.size() == 1) {
            return CollectionUtil.get(map0.keySet(), 0);
        }
        final double sum = sum(source);
        final double rndDraw = rnd.nextDouble() * sum;
        double temp = 0.0;
        T draw = null;
        for(Map.Entry<T, W> entry: map0.entrySet()) {
            temp += weightFunction.applyAsDouble(entry.getValue());
            draw = entry.getKey();
            if(rndDraw < temp) {
                return draw;
            }
        }
        return draw;
    }
}

/*
[0,1) -> x
drawValue = sum * x = (a+b+c..) * x = ax+bx+cx..

X


 */