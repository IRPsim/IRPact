package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;

import java.util.*;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class BasicAffinities<T> implements Affinities<T> {

    protected WeightedMapping<T> mapping;

    public BasicAffinities() {
        this(new NavigableMapWeightedMapping<>());
    }

    public BasicAffinities(WeightedMapping<T> mapping) {
        this.mapping = mapping;
    }

    protected BasicAffinities<T> newInstance(WeightedMapping<T> copy) {
        return new BasicAffinities<>(copy);
    }

    @Override
    public Affinities<T> createWithout(T target) {
        WeightedMapping<T> copy = mapping.copyWithout(target);
        return newInstance(copy);
    }

    @Override
    public Collection<T> targets() {
        return mapping.elements();
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
    public boolean hasValue(T target) {
        return mapping.has(target);
    }

    @Override
    public boolean remove(T target) {
        return mapping.remove(target);
    }

    @Override
    public double getValue(T target) {
        return mapping.getWeight(target);
    }

    @Override
    public void setValue(T target, double value) {
        mapping.set(target, value);
    }

    @Override
    public double sum() {
        return mapping.totalWeight();
    }

    @Override
    public T getRandom(Rnd rnd) {
        return mapping.getRandom(rnd);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        return mapping.getWeightedRandom(rnd);
    }
}
