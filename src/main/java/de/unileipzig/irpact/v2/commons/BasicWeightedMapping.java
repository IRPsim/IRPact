package de.unileipzig.irpact.v2.commons;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @param <S>
 * @param <T>
 * @param <W>
 * @author Daniel Abitz
 */
public class BasicWeightedMapping<S, T, W> implements WeightedMapping<S, T, W> {

    protected Supplier<? extends Map<S, Map<T, W>>> mappingSupplier;
    protected Supplier<? extends Map<T, W>> map0Supplier;
    protected Map<S, Map<T, W>> mapping;

    public BasicWeightedMapping() {
        this(LinkedHashMap::new, LinkedHashMap::new);
    }

    public BasicWeightedMapping(
            Supplier<? extends Map<S, Map<T, W>>> mappingSupplier,
            Supplier<? extends Map<T, W>> map0Supplier) {
        this.mappingSupplier = mappingSupplier;
        this.map0Supplier = map0Supplier;
        mapping = mappingSupplier.get();
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
}
