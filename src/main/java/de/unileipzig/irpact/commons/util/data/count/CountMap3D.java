package de.unileipzig.irpact.commons.util.data.count;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class CountMap3D<A, B, C> {

    protected MapSupplier mapSupplier;
    protected Map<A, Map<B, Map<C, Integer>>> counter;

    public CountMap3D() {
        this(MapSupplier.CONCURRENT_HASH);
    }

    public CountMap3D(MapSupplier mapSupplier) {
        this.mapSupplier = mapSupplier;
        counter = mapSupplier.newMap();
    }

    public CountMap3D(CountMap3D<A, B, C> other) {
        this(other.mapSupplier);
        set(other.counter);
    }

    public CountMap3D<A, B, C> copy() {
        return new CountMap3D<>(this);
    }

    public MapSupplier supplier() {
        return mapSupplier;
    }

    public Set<A> getFirstKeys() {
        return counter.keySet();
    }

    public Set<B> getAllSecondKeys() {
        Set<B> set = new LinkedHashSet<>();
        getAllSecondsKeys(set);
        return set;
    }

    public boolean getAllSecondsKeys(Collection<? super B> target) {
        boolean changed = false;
        for(Map<B, ?> map: counter.values()) {
            changed |= target.addAll(map.keySet());
        }
        return changed;
    }

    public Set<C> getAllThirdKeys() {
        Set<C> set = new LinkedHashSet<>();
        getAllThirdKeys(set);
        return set;
    }

    public boolean getAllThirdKeys(Collection<? super C> target) {
        boolean changed = false;
        for(Map<?, Map<C, Integer>> bcmap: counter.values()) {
            for(Map<C, ?> cmap: bcmap.values()) {
                changed |= target.addAll(cmap.keySet());
            }
        }
        return changed;
    }

    public Map<A, Map<B, Map<C, Integer>>> map() {
        return counter;
    }

    protected Map<C, Integer> map(A a, B b) {
        return counter.computeIfAbsent(a, _a -> mapSupplier.newMap())
                .computeIfAbsent(b, _b -> mapSupplier.newMap());
    }

    public Map<C, Integer> get(A a, B b) {
        Map<B, Map<C, Integer>> map = counter.get(a);
        return map == null
                ? null
                : map.get(b);
    }

    public void set(Map<A, Map<B, Map<C, Integer>>> other) {
        counter.clear();
        for(Map.Entry<A, Map<B, Map<C, Integer>>> abcEntry: other.entrySet()) {
            for(Map.Entry<B, Map<C, Integer>> bcEntry: abcEntry.getValue().entrySet()) {
                for(Map.Entry<C, Integer> cEntry: bcEntry.getValue().entrySet()) {
                    init(abcEntry.getKey(), bcEntry.getKey(), cEntry.getKey(), cEntry.getValue());
                }
            }
        }
    }

    public void init(A a, B b, C c) {
        init(a, b, c, 0);
    }

    public void init(A a, B b, C c, int initial) {
        map(a, b).put(c, initial);
    }

    public void update(A a, B b, C c) {
        update(a, b, c, 1);
    }

    public void update(A a, B b, C c, int delta) {
        Map<C, Integer> map = map(a, b);
        int current = map.getOrDefault(c, 0);
        map.put(c, current + delta);
    }

    public int getCount(A a, B b, C c) {
        Map<C, Integer> map = get(a, b);
        return map == null
                ? 0
                : map.getOrDefault(c, 0);
    }

    public void add(CountMap3D<A, B, C> other) {
        update(other, 1);
    }

    public void sub(CountMap3D<A, B, C> other) {
        update(other, -1);
    }

    protected void update(CountMap3D<A, B, C> other, int fac) {
        for(Map.Entry<A, Map<B, Map<C, Integer>>> abcEntry: other.counter.entrySet()) {
            for(Map.Entry<B, Map<C, Integer>> bcEntry: abcEntry.getValue().entrySet()) {
                for(Map.Entry<C, Integer> cEntry: bcEntry.getValue().entrySet()) {
                    update(abcEntry.getKey(), bcEntry.getKey(), cEntry.getKey(), cEntry.getValue() * fac);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CountMap3D{" +
                "counter=" + counter +
                '}';
    }
}
