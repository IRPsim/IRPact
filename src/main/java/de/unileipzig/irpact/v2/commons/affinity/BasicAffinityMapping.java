package de.unileipzig.irpact.v2.commons.affinity;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @param <S>
 * @param <T>
 * @author Daniel Abitz
 */
public class BasicAffinityMapping<S, T> implements AffinityMapping<S, T> {

    protected Map<S, Affinities<T>> mapping;

    public BasicAffinityMapping() {
        this(new HashMap<>());
    }

    public BasicAffinityMapping(Map<S, Affinities<T>> mapping) {
        this.mapping = mapping;
    }

    @Override
    public boolean has(S source) {
        return mapping.containsKey(source);
    }

    @Override
    public boolean has(S source, T target) {
        Affinities<T> affinities = get(source);
        return affinities != null && affinities.hasValue(target);
    }

    @Override
    public Affinities<T> get(S source) {
        return mapping.get(source);
    }

    @Override
    public double get(S source, T target) {
        Affinities<T> affinities = get(source);
        if(affinities == null) {
            throw new NoSuchElementException();
        }
        return affinities.getValue(target);
    }

    protected Affinities<T> newAffinitiesInstance() {
        return new BasicAffinities<>();
    }

    @Override
    public void put(S source, T target, double value) {
        Affinities<T> affinities = mapping.computeIfAbsent(source, _source -> newAffinitiesInstance());
        affinities.setValue(target, value);
    }

    @Override
    public void put(S source, Affinities<T> affinities) {
        mapping.put(source, affinities);
    }
}