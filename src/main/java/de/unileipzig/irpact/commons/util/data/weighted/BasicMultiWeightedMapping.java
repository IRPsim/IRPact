package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class BasicMultiWeightedMapping<S, T> implements MultiWeightedMapping<S, T> {

    protected Function<? super S, ? extends WeightedMapping<T>> mappingCreator;
    protected Map<S, WeightedMapping<T>> mappings;

    public BasicMultiWeightedMapping(Supplier<? extends WeightedMapping<T>> mappingSupplier) {
        this(source -> mappingSupplier.get());
    }

    public BasicMultiWeightedMapping(Function<? super S, ? extends WeightedMapping<T>> mappingCreator) {
        this(mappingCreator, new LinkedHashMap<>());
    }

    public BasicMultiWeightedMapping(
            Function<? super S, ? extends WeightedMapping<T>> mappingCreator,
            Map<S, WeightedMapping<T>> mappings) {
        this.mappingCreator = mappingCreator;
        this.mappings = mappings;
    }

    @Override
    public boolean isEmpty() {
        return mappings.isEmpty();
    }

    @Override
    public int size() {
        return mappings.size();
    }

    @Override
    public int size(S source) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            return 0;
        }
        return mapping.size();
    }

    @Override
    public boolean has(S source) {
        return mappings.containsKey(source);
    }

    @Override
    public boolean has(S source, T target) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            return false;
        }
        return mapping.has(target);
    }

    @Override
    public double getWeight(S source, T target) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            return 0;
        }
        return mapping.getWeight(target);
    }

    @Override
    public boolean remove(S source) {
        return mappings.remove(source) != null;
    }

    @Override
    public boolean remove(S source, T target) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            return false;
        }
        return mapping.remove(target);
    }

    @Override
    public void set(S source, T target, double weight) {
        WeightedMapping<T> mapping = getMapping(source);
        mapping.set(target, weight);
    }

    @Override
    public void set(S source, WeightedValue<T> value) {
        WeightedMapping<T> mapping = getMapping(source);
        mapping.set(value);
    }

    public WeightedMapping<T> getMapping(S source) {
        return mappings.computeIfAbsent(source, mappingCreator);
    }

    @Override
    public T getRandom(S source, Rnd rnd) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            throw new NoSuchElementException();
        }
        return mapping.getRandom(rnd);
    }

    @Override
    public T getWeightedRandom(S source, Rnd rnd) {
        WeightedMapping<T> mapping = mappings.get(source);
        if(mapping == null) {
            throw new NoSuchElementException();
        }
        return mapping.getWeightedRandom(rnd);
    }
}
