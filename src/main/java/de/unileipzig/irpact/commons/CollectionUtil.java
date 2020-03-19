package de.unileipzig.irpact.commons;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class CollectionUtil {

    private CollectionUtil() {
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

    @SuppressWarnings("unchecked")
    public static <T> Set<T> hashSetOf(T... values) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, values);
        return set;
    }
}
