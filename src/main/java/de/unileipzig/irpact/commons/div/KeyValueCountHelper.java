package de.unileipzig.irpact.commons.div;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.v2.commons.Pair;

import java.util.*;
import java.util.function.Supplier;

/**
 * Combines a Key-Value Map with a counter.
 * @author Daniel Abitz
 */
public class KeyValueCountHelper<K, V> {

    private Supplier<? extends Map<Integer, Set<V>>> countMapSupplier;
    private Supplier<? extends Set<V>> setSupplier;
    private Map<K, Map<Integer, Set<V>>> map;

    public KeyValueCountHelper(
            Supplier<? extends Map<Integer, Set<V>>> countMapSupplier,
            Supplier<? extends Set<V>> setSupplier,
            Map<K, Map<Integer, Set<V>>> map) {
        this.countMapSupplier = countMapSupplier;
        this.setSupplier = setSupplier;
        this.map = map;
    }

    public void initialize(K key, V value, int count) {
        map.computeIfAbsent(key, _key -> countMapSupplier.get())
                .computeIfAbsent(count, _count -> setSupplier.get())
                .add(value);
    }

    private Pair<K, Integer> findKeyAndCount(V value) {
        for(Map.Entry<K, Map<Integer, Set<V>>> entry: map.entrySet()) {
            for(Map.Entry<Integer, Set<V>> entry2: entry.getValue().entrySet()) {
                if(entry2.getValue().contains(value)) {
                    return Pair.get(entry.getKey(), entry2.getKey());
                }
            }
        }
        throw new NoSuchElementException();
    }

    public void update(V value, int count) {
        Pair<K, Integer> pair = findKeyAndCount(value);
        Set<V> removeFrom = map.get(pair.first())
                .get(pair.second());
        if(removeFrom != null) {
            removeFrom.remove(value);
            if(removeFrom.isEmpty()) {
                map.get(pair.first())
                        .remove(pair.second());
            }
        }
        map.get(pair.first())
                .computeIfAbsent(pair.second() + count, _count -> setSupplier.get())
                .add(value);
    }

    public int getCount(V value) {
        Pair<K, Integer> pair = findKeyAndCount(value);
        return pair.second();
    }

    public List<V> listFirstValues(K key) {
        List<V> list = new ArrayList<>();
        getFristValues(key, list);
        return list;
    }

    public void getFristValues(K key, Collection<V> target) {
        Map<Integer, Set<V>> countMap = map.get(key);
        Set<V> firstSet = CollectionUtil.getFirst(countMap.values());
        target.addAll(firstSet);
    }
}
