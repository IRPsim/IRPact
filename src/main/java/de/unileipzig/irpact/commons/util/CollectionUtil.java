package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.util.data.WeightedData;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static List<Object> listAll(Map<?, ?> map) {
        List<Object> objs = new ArrayList<>();
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            addTo(key, objs);
            addTo(value, objs);
        }
        return objs;
    }

    private static void addTo(Object obj, Collection<Object> target) {
        if(obj instanceof Collection) {
            addTo((Collection<?>) obj, target);
        }
        else {
            target.add(obj);
        }
    }

    private static void addTo(Collection<?> coll, Collection<Object> target) {
        for(Object obj: coll) {
            addTo(obj, target);
        }
    }

    public static <K, V> void sortMap(
            Map<K, V> map,
            Comparator<? super K> keyComparator) {
        List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort((o1, o2) -> {
            K k1 = o1.getKey();
            K k2 = o2.getKey();
            return keyComparator.compare(k1, k2);
        });
        map.clear();
        for(Map.Entry<K, V> entry: entryList) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public static <E> E[][] transpose(
            E[][] input,
            BiFunction<? super Number, ? super Number, ? extends E[][]> arrayCreator) {
        E[][] output = arrayCreator.apply(input[0].length, input.length);
        for(int i = 0; i < input.length; i++) {
            for(int j = 0; j < input[i].length; j++) {
                output[j][i] = input[i][j];
            }
        }
        return output;
    }

    public static <E> List<E> filterToList(Collection<? extends E> input, Predicate<? super E> filter) {
        List<E> list = new ArrayList<>();
        filter(input, filter, list);
        return list;
    }

    public static <E> Set<E> filterToSet(Collection<? extends E> input, Predicate<? super E> filter) {
        Set<E> list = newSet();
        filter(input, filter, list);
        return list;
    }

    public static <E> boolean filter(Collection<? extends E> input, Predicate<? super E> filter, Collection<? super E> output) {
        boolean changed = false;
        for(E element: input) {
            if(filter.test(element)) {
                changed |= output.add(element);
            }
        }
        return changed;
    }

    public static <T> boolean containsSame(T[] array, T object) {
        for(T t: array) {
            if(t == object) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean containsEquals(T[] array, T object) {
        for(T t: array) {
            if(Objects.equals(t, object)) {
                return true;
            }
        }
        return false;
    }

    public static <K, V> Map<K, V> newMap() {
        return new LinkedHashMap<>();
    }

    public static <E> Set<E> newSet() {
        return new LinkedHashSet<>();
    }

    public static <K, V> Supplier<Map<K, V>> newMapSupplier() {
        return CollectionUtil::newMap;
    }

    public static <A, K, V> Function<A, Map<K, V>> newMapFunction() {
        return a -> newMap();
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

    public static <T> Collector<T, ?, LinkedHashSet<T>> collectToLinkedSet() {
        return Collectors.toCollection(LinkedHashSet::new);
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
        int i = 0;
        for(T value: coll) {
            if(i == index) {
                coll.remove(value);
                return value;
            }
            i++;
        }
        throw new IllegalStateException("index not found"); //unerreichbar
    }

    public static <T> T getFirst(Collection<T> coll) {
        return get(coll, 0);
    }

    public static <T> T getRandom(List<T> list, Rnd rnd) {
        return getRandom(list, 0, list.size(), rnd);
    }

    public static <T> T getRandom(List<T> list, int from, int to, Rnd rnd) {
        int index = rnd.nextInt(from, to);
        return list.get(index);
    }

    public static <T> T getRandom(Collection<T> coll, Rnd rnd) {
        return getRandom(coll, 0, coll.size(), rnd);
    }

    public static <T> T getRandom(Collection<T> coll, int from, int to, Rnd rnd) {
        int index = rnd.nextInt(from, to);
        return get(coll, index);
    }

    public static <T> T removeRandom(Collection<T> coll, Rnd rnd) {
        return removeRandom(coll, 0, coll.size(), rnd);
    }

    public static <T> T removeRandom(Collection<T> coll, int from, int to, Rnd rnd) {
        int index = rnd.nextInt(from, to);
        return remove(coll, index);
    }

    public static <T> T getWeightedRandom(
            Collection<? extends T> coll,
            ToDoubleFunction<? super T> weightFunction,
            Rnd rnd) {
        double sum = coll.stream()
                .mapToDouble(weightFunction)
                .sum();
        return getWeightedRandom(coll, weightFunction, sum, rnd);
    }

    public static <T> T getWeightedRandom(
            Collection<? extends T> coll,
            ToDoubleFunction<? super T> weightFunction,
            double sum,
            Rnd rnd) {
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
            ValueAndIntToDoubleFunction<? super T> weightFunction,
            Rnd rnd) {
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
            ValueAndIntToDoubleFunction<? super T> weightFunction,
            double sum,
            Rnd rnd) {
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

    public static <K, V> V getNonNull(Map<K, V> map, K key) {
        V value = map.get(key);
        if(value == null) {
            throw new NullPointerException(Objects.toString(key));
        }
        return value;
    }

    public static <T, K> List<T> weightedSort(
            List<T> input,
            Function<? super T, ? extends K> classifier,
            List<T> output) {
        WeightedData<K> wclasses = new WeightedData<>();
        for(T data: input) {
            K k = classifier.apply(data);
            wclasses.update(k, 1.0);
        }


        return output;
    }
}
