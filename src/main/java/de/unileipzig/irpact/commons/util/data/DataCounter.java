package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class DataCounter<K> {

    protected static final Comparator<Integer> ASC = Integer::compareTo;
    protected static final Comparator<Integer> DESC = ((Comparator<Integer>) Integer::compareTo).reversed();

    protected static final Function<?, ? extends Integer> ZERO = k -> 0;
    @SuppressWarnings("unchecked")
    protected static <K> Function<K, ? extends Integer> zero() {
        return (Function<K, ? extends Integer>) ZERO;
    }

    protected Map<K, Integer> counter;
    protected boolean allowNegative = false;

    public DataCounter() {
        this(new HashMap<>());
    }

    public DataCounter(Map<K, Integer> counter) {
        this.counter = counter;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public boolean allowNegative() {
        return allowNegative;
    }

    public boolean onlyPositive() {
        return !allowNegative;
    }

    public Collection<K> keys() {
        return counter.keySet();
    }

    public int get(K key) {
        Integer count = counter.get(key);
        return count == null ? 0 : count;
    }

    public double getShare(K key) {
        return (double) get(key) / (double) total();
    }

    public void set(K key, int count) {
        counter.put(key, count);
    }

    public void update(K key, int delta) {
        int current = counter.computeIfAbsent(key, zero());
        int newValue = current + delta;
        if(onlyPositive()) {
            newValue = Math.max(0, newValue);
        }
        counter.put(key, newValue);
    }

    public void inc(K key) {
        update(key, 1);
    }

    public void inc(K key, int delta) {
        update(key, delta);
    }

    public void dec(K key) {
        update(key, -1);
    }

    public void dec(K key, int delta) {
        update(key, -delta);
    }

    public int total() {
        return counter.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public void sortKey(Comparator<? super K> keyComparator) {
        if(!(counter instanceof LinkedHashMap)) {
            counter = new LinkedHashMap<>(counter);
        }
        CollectionUtil.sortMapAfterKey(counter, keyComparator);
    }

    public void sortValue(Comparator<? super Integer> keyComparator) {
        if(!(counter instanceof LinkedHashMap)) {
            counter = new LinkedHashMap<>(counter);
        }
        CollectionUtil.sortMapAfterValue(counter, keyComparator);
    }

    public void sortValueAscending() {
        sortValue(ASC);
    }

    public void sortValueDescending() {
        sortValue(DESC);
    }

    public void forEach(BiConsumer<? super K, ? super Integer> action) {
        counter.forEach(action);
    }

    public String printCounterMap(Function<? super K, ? extends String> toString) {
        Map<String, Integer> m = new HashMap<>();
        for(Map.Entry<K, Integer> entry: counter.entrySet()) {
            m.put(toString.apply(entry.getKey()), entry.getValue());
        }
        return m.toString();
    }
}
