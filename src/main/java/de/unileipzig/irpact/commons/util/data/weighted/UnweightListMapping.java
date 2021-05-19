package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class UnweightListMapping<T> implements WeightedMapping<T> {

    protected Supplier<? extends List<T>> listSupplier;
    protected List<T> values;

    public UnweightListMapping() {
        this(ArrayList::new);
    }

    public UnweightListMapping(Supplier<? extends List<T>> listSupplier) {
        this(listSupplier, listSupplier.get());
    }

    public UnweightListMapping(Supplier<? extends List<T>> listSupplier, List<T> values) {
        this.listSupplier = listSupplier;
        this.values = values;
    }

    @Override
    public WeightedMapping<T> copy() {
        List<T> copy = listSupplier.get();
        copy.addAll(values);
        return new UnweightListMapping<>(listSupplier, copy);
    }

    @Override
    public WeightedMapping<T> copyWithout(T toRemove) {
        List<T> copy = listSupplier.get();
        copy.addAll(values);
        copy.remove(toRemove);
        return new UnweightListMapping<>(listSupplier, copy);
    }

    @Override
    public Collection<T> elements() {
        return values;
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
    public void clear() {
        values.clear();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean allowsZeroWeight() {
        return true;
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

    @Override
    public boolean removeAll(Collection<? extends T> targets) {
        return values.removeAll(targets);
    }

    public void add(T target) {
        values.add(target);
    }

    public void addAll(Collection<? extends T> coll) {
        values.addAll(coll);
    }

    @Override
    public double totalWeight() {
        return 0;
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
