package de.unileipzig.irpact.commons;

import java.util.*;
import java.util.function.ToDoubleFunction;

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

    @SafeVarargs
    public static <T> Set<T> linkedHashSetOf(T... values) {
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
        if(coll instanceof List) {
            return ((List<T>) coll).get(index);
        }
        int i = 0;
        for(T t: coll) {
            if(i == index) {
                return t;
            }
            i++;
        }
        throw new IndexOutOfBoundsException("index: " + i + ", size: " + coll.size());
    }

    public static <T> T remove(Collection<T> coll, int index) {
        if(coll instanceof List) {
            List<T> list = (List<T>) coll;
            return list.remove(index);
        }
        if(index < 0 || index >= coll.size()) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + coll.size());
        }
        Ref<Integer> counter = new Ref<>(0);
        Ref<T> value = new Ref<>();
        coll.removeIf(t -> {
            int current = counter.get();
            if(current == -1) return false;
            counter.set(current + 1);
            if(current == index) {
                counter.set(-1);
                value.set(t);
                return true;
            } else {
                return false;
            }
        });
        return value.get();
    }

    public static <T> T getFirst(Collection<T> coll) {
        return get(coll, 0);
    }

    public static <T> T getRandom(List<T> list, Random rnd) {
        return getRandom(list, 0, list.size(), rnd);
    }

    public static <T> T getRandom(List<T> list, int from, int to, Random rnd) {
        int index = Util.nextInt(rnd, from, to);
        return list.get(index);
    }

    public static <T> T getRandom(Collection<T> coll, Random rnd) {
        return getRandom(coll, 0, coll.size(), rnd);
    }

    public static <T> T getRandom(Collection<T> coll, int from, int to, Random rnd) {
        int index = Util.nextInt(rnd, from, to);
        return get(coll, index);
    }

    public static <T> T removeRandom(Collection<T> coll, Random rnd) {
        return removeRandom(coll, 0, coll.size(), rnd);
    }

    public static <T> T removeRandom(Collection<T> coll, int from, int to, Random rnd) {
        int index = Util.nextInt(rnd, from, to);
        return remove(coll, index);
    }

    public static <T> T getWeightedRandom(
            Collection<? extends T> coll,
            ToDoubleFunction<? super T> weightFunction,
            Random rnd) {
        double sum = coll.stream()
                .mapToDouble(weightFunction)
                .sum();
        return getWeightedRandom(coll, weightFunction, sum, rnd);
    }

    public static <T> T getWeightedRandom(
            Collection<? extends T> coll,
            ToDoubleFunction<? super T> weightFunction,
            double sum,
            Random rnd) {
        final double rndDraw = rnd.nextDouble() * sum;
        double temp = 0.0;
        T result = null;
        for(T value: coll) {
            temp += weightFunction.applyAsDouble(value);
            result = value;
            if(rndDraw < temp) {
                return result;
            }
        }
        return result;
    }

    public static <T> int getWeightedRandomIndex(
            Collection<? extends T> coll,
            ValueIntToDoubleFunction<? super T> weightFunction,
            Random rnd) {
        double sum = 0.0;
        int i = 0;
        for(T value: coll) {
            sum += weightFunction.applyAsDouble(value, i);
            i++;
        }
        return getWeightedRandomIndex(coll, weightFunction, sum, rnd);
    }

    public static <T> int getWeightedRandomIndex(
            Collection<? extends T> coll,
            ValueIntToDoubleFunction<? super T> weightFunction,
            double sum,
            Random rnd) {
        final double rndDraw = rnd.nextDouble() * sum;
        double temp = 0.0;
        int i = 0;
        for(T value: coll) {
            temp += weightFunction.applyAsDouble(value, i);
            if(rndDraw < temp) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
