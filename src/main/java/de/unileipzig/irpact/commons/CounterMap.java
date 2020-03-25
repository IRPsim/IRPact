package de.unileipzig.irpact.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CounterMap<E> {

    private Map<E, Integer> map = new HashMap<>();

    public CounterMap() {
    }

    public void inc(E element) {
        inc(element, 1);
    }

    public void inc(E element, int count) {
        int current = map.computeIfAbsent(element, _element -> 0);
        map.put(element, current + count);
    }

    public int getCount(E element) {
        Integer count = map.get(element);
        return count == null
                ? 0
                : count;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
