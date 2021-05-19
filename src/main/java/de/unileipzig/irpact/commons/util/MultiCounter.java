package de.unileipzig.irpact.commons.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class MultiCounter {

    protected Map<String, Integer> counterMap;

    public MultiCounter() {
        this(new HashMap<>());
    }

    public MultiCounter(Map<String, Integer> counterMap) {
        this.counterMap = counterMap;
    }

    public int getAndInc(String key) {
        int current = counterMap.computeIfAbsent(key, _key -> 0);
        counterMap.put(key, current + 1);
        return current;
    }
}
