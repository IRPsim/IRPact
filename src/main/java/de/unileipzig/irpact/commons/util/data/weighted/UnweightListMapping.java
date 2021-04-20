package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.WeightedValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UnweightListMapping<T> implements WeightedMapping<T> {

    protected List<T> values;

    public UnweightListMapping() {
        this(new ArrayList<>());
    }

    public UnweightListMapping(List<T> values) {
        this.values = values;
    }

    public void sort() {
        values.sort(null);
    }

    public void sort(Comparator<? super T> comparator) {
        values.sort(comparator);
    }

    public void shuffle(Rnd rnd) {
        rnd.shuffle(values);
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public boolean has(T target) {
        return values.contains(target);
    }

    @Override
    public double getWeight(T target) {
        return 0;
    }

    @Override
    public boolean remove(T target) {
        return values.remove(target);
    }

    public void add(T target) {
        values.add(target);
    }

    @Override
    public void set(T target, double weight) {
        if(!has(target)) {
            add(target);
        }
    }

    @Override
    public void set(WeightedValue<T> value) {
        set(value.getValue(), value.getWeight());
    }

    protected void requiresNotEmpty() {
        if(values.isEmpty()) {
            throw new IllegalStateException("empty");
        }
    }

    @Override
    public T getRandom(Rnd rnd) {
        requiresNotEmpty();
        return rnd.getRandom(values);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        return getRandom(rnd);
    }
}
