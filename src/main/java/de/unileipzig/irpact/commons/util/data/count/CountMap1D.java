package de.unileipzig.irpact.commons.util.data.count;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class CountMap1D<T> {

    protected Map<T, Integer> counter = new HashMap<>();

    public CountMap1D() {
    }

    protected CountMap1D(CountMap1D<T> other) {
        this.counter.putAll(other.counter);
    }

    public CountMap1D<T> copy() {
        return new CountMap1D<>(this);
    }

    public void set(CountMap1D<T> other) {
        counter.clear();
        counter.putAll(other.counter);
    }

    public void set(T key, int count) {
        counter.put(key, count);
    }

    public void init(T key) {
        set(key, 0);
    }

    public void init(Iterable<? extends T> keys) {
        for(T key: keys) {
            init(key);
        }
    }

    public void update(T key) {
        update(key, 1);
    }

    protected void update(T key, int delta) {
        int current = counter.getOrDefault(key, 0);
        counter.put(key, current + delta);
    }

    public int getCount(T key) {
        return counter.getOrDefault(key, 0);
    }

    public int getAndUpdate(T key) {
        int current = getCount(key);
        update(key);
        return current;
    }

    public void add(CountMap1D<T> other) {
        for(Map.Entry<T, Integer> entry: other.counter.entrySet()) {
            update(entry.getKey(), entry.getValue());
        }
    }

    public void sub(CountMap1D<T> other) {
        for(Map.Entry<T, Integer> entry: other.counter.entrySet()) {
            update(entry.getKey(), -entry.getValue());
        }
    }

    public Set<T> keys() {
        return counter.keySet();
    }

    public CountMap1D<T> cumulate(Comparator<? super T> c) {
        List<T> keys = new ArrayList<>(keys());
        keys.sort(c);
        return cumulate(keys);
    }

    public CountMap1D<T> cumulate(List<? extends T> sortedKeys) {
        CountMap1D<T> cumulated = new CountMap1D<>();

        if(sortedKeys.size() == 1) {
            cumulated.set(sortedKeys.get(0), getCount(sortedKeys.get(0)));
            return cumulated;
        }

        for(int i = 0; i < sortedKeys.size(); i++) {
            T key = sortedKeys.get(i);
            int count = getCount(key);
            if(i > 0) {
                T preKey = sortedKeys.get(i - 1);
                int preCount = cumulated.getCount(preKey);
                cumulated.set(key, preCount + count);
            } else {
                cumulated.set(key, count);
            }
        }

        return cumulated;
    }

    public CountMap1D<T> uncumulate(Comparator<? super T> c) {
        List<T> keys = new ArrayList<>(keys());
        keys.sort(c);
        return uncumulate(keys);
    }

    public CountMap1D<T> uncumulate(List<? extends T> sortedKeys) {
        CountMap1D<T> uncumulated = new CountMap1D<>();

        if(sortedKeys.size() == 1) {
            uncumulated.set(sortedKeys.get(0), getCount(sortedKeys.get(0)));
            return uncumulated;
        }

        for(int i = 0; i < sortedKeys.size(); i++) {
            T key = sortedKeys.get(i);
            int count = getCount(key);
            if(i > 0) {
                T preKey = sortedKeys.get(i - 1);
                int preCount = getCount(preKey);
                uncumulated.set(key, count - preCount);
            } else {
                uncumulated.set(key, count);
            }
        }

        return uncumulated;
    }

    @Override
    public String toString() {
        return "CountMap1D{" +
                "counter=" + counter +
                '}';
    }
}
