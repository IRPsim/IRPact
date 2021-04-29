package de.unileipzig.irpact.commons.util.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class DataCounter<K> {

    protected final Function<? super K, ? extends Integer> INIT = k -> 0;
    protected Map<K, Integer> counter;

    public DataCounter() {
        this(new HashMap<>());
    }

    public DataCounter(Map<K, Integer> counter) {
        this.counter = counter;
    }

    public Collection<K> keys() {
        return counter.keySet();
    }

    public int get(K key) {
        Integer count = counter.get(key);
        return count == null ? 0 : count;
    }

    public void set(K key, int count) {
        counter.put(key, count);
    }

    public void update(K key, int delta) {
        int current = counter.computeIfAbsent(key, INIT);
        int minZero = Math.max(0, current + delta);
        counter.put(key, minZero);
    }

    public void inc(K key) {
        update(key, 1);
    }

    public void dec(K key) {
        update(key, -1);
    }

    public int total() {
        return counter.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public void forEach(BiConsumer<? super K, ? super Integer> action) {
        counter.forEach(action);
    }
}
