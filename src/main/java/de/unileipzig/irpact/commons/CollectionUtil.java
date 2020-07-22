package de.unileipzig.irpact.commons;

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

    public static <T> T getFirst(Collection<T> coll) {
        for(T t: coll) {
            return t;
        }
        throw new NoSuchElementException();
    }

    public static <T> T getFirst(Collection<T> coll, T ifEmpty) {
        for(T t: coll) {
            return t;
        }
        return ifEmpty;
    }

    public static <T> T get(Collection<T> coll, int index) {
        int i = 0;
        for(T t: coll) {
            if(i == index) {
                return t;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    public static <T> T get(Collection<T> coll, Random rnd) {
        int index = rnd.nextInt(coll.size());
        return get(coll, index);
    }

    @SafeVarargs
    public static <T> Set<T> hashSetOf(T... values) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, values);
        return set;
    }

    @SafeVarargs
    public static <T> List<T> arrayListOf(T... values) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    public static <T> T getRandom(List<T> list, Random rnd) {
        return getRandom(list, 0, list.size(), rnd);
    }

    public static <T> T getRandom(List<T> list, int from, int to, Random rnd) {
        int index = rnd.nextInt(to - from) + from;
        return list.get(index);
    }

    public static <T> T getRandom(Collection<T> list, Random rnd) {
        return getRandom(list, 0, list.size(), rnd);
    }

    public static <T> T getRandom(Collection<T> list, int from, int to, Random rnd) {
        int index = rnd.nextInt(to - from) + from;
        int i = 0;
        for(T item: list) {
            if(i == index) {
                return item;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }
}
