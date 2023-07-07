package de.unileipzig.irpact.commons.util.data.count;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CountMap2D<A, B> {

    protected MapSupplier mapSupplier;
    protected Map<A, Map<B, Integer>> counter;

    public CountMap2D() {
        this(MapSupplier.CONCURRENT_HASH);
    }

    public CountMap2D(MapSupplier mapSupplier) {
        this.mapSupplier = mapSupplier;
        counter = mapSupplier.newMap();
    }

    protected CountMap2D(CountMap2D<A, B> other) {
        this(other.mapSupplier);
        set(other.counter);
    }

    public MapSupplier supplier() {
        return mapSupplier;
    }

    public boolean getAllSecondsKeys(Collection<? super B> target) {
        boolean changed = false;
        for(Map<B, ?> map: counter.values()) {
            changed |= target.addAll(map.keySet());
        }
        return changed;
    }

    public Map<A, Map<B, Integer>> map() {
        return counter;
    }

    protected Map<B, Integer> map(A a) {
        return counter.computeIfAbsent(a, _a -> new HashMap<>());
    }

    protected Map<B, Integer> get(A a) {
        return counter.get(a);
    }

    public CountMap2D<A, B> copy() {
        return new CountMap2D<>(this);
    }

    public void set(Map<A, Map<B, Integer>> other) {
        counter.clear();
        for(Map.Entry<A, Map<B, Integer>> abcEntry: other.entrySet()) {
            for(Map.Entry<B, Integer> bcEntry: abcEntry.getValue().entrySet()) {
                init(abcEntry.getKey(), bcEntry.getKey(), bcEntry.getValue());
            }
        }
    }

    public void init(A a, B b) {
        init(a, b, 0);
    }

    public void init(A a, B b, int initial) {
        map(a).put(b, initial);
    }

    public void update(A a, B b) {
        update(a, b, 1);
    }

    protected void update(A a, B b, int delta) {
        Map<B, Integer> map = map(a);
        int current = map.getOrDefault(b, 0);
        map.put(b, current + delta);
    }

    public int getCount(A a, B b) {
        Map<B, Integer> map = get(a);
        return map == null
                ? 0
                : map.getOrDefault(b, 0);
    }

    public void add(CountMap2D<A, B> other) {
        update(other, 1);
    }

    public void sub(CountMap2D<A, B> other) {
        update(other, -1);
    }

    protected void update(CountMap2D<A, B> other, int fac) {
        for(Map.Entry<A, Map<B, Integer>> aentry: other.counter.entrySet()) {
            for(Map.Entry<B, Integer> bentry: aentry.getValue().entrySet()) {
                update(aentry.getKey(), bentry.getKey(), bentry.getValue() * fac);
            }
        }
    }

    @Override
    public String toString() {
        return "CountMap2D{" +
                "counter=" + counter +
                '}';
    }
}
