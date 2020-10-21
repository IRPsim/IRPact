package de.unileipzig.irpact.v2.commons;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <T> List<T> toList(Iterator<? extends T> iter) {
        List<T> list = new ArrayList<>();
        addAll(list, iter);
        return list;
    }

    public static <T> Set<T> toSet(Iterator<? extends T> iter) {
        Set<T> list = new HashSet<>();
        addAll(list, iter);
        return list;
    }

    public static <T> boolean addAll(Collection<? super T> coll, Iterator<? extends T> iter) {
        boolean changed = false;
        while(iter.hasNext()) {
            changed |= coll.add(iter.next());
        }
        return changed;
    }

    @SafeVarargs
    public static <T> Set<T> hashSetOf(T... values) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, values);
        return set;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> hashMapOf(Object... values) {
        if(values.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<K, V> map = new HashMap<>();
        for(int i = 0; i < values.length; i += 2) {
            K key = (K) values[i];
            V value = (V) values[i + 1];
            map.put(key, value);
        }
        return map;
    }

    @SafeVarargs
    public static <T> List<T> arrayListOf(T... values) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    public static <T> T get(Collection<T> coll, int index) {
        int i = 0;
        for(T t: coll) {
            if(i == index) {
                return t;
            }
            i++;
        }
        throw new IndexOutOfBoundsException("index: " + i + ", size: " + coll.size());
    }

    public static <T> T getFirst(Collection<T> coll) {
        return get(coll, 0);
    }

    public static <T> T getRandom(List<T> list, Random rnd) {
        return getRandom(list, 0, list.size(), rnd);
    }

    public static <T> T getRandom(List<T> list, int from, int to, Random rnd) {
        int index = rnd.nextInt(to - from) + from;
        return list.get(index);
    }

    public static <T> T getRandom(Collection<T> coll, Random rnd) {
        return getRandom(coll, 0, coll.size(), rnd);
    }

    public static <T> T getRandom(Collection<T> coll, int from, int to, Random rnd) {
        int index = rnd.nextInt(to - from) + from;
        return get(coll, index);
    }
}
