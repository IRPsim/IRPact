package de.unileipzig.irpact.commons.util.data.count;

import java.util.HashMap;
import java.util.Map;

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

    public void init(T key) {
        counter.put(key, 0);
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

    @Override
    public String toString() {
        return "CountMap1D{" +
                "counter=" + counter +
                '}';
    }
}
