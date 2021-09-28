package de.unileipzig.irpact.commons.util.data.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class Map5<A, B, C, D, E> {

    protected Function<? super A, ? extends Map<B, Map<C, Map<D, E>>>> map0Creator;
    protected Function<? super B, ? extends Map<C, Map<D, E>>> map1Creator;
    protected Function<? super C, ? extends Map<D, E>> map2Creator;
    protected Map<A, Map<B, Map<C, Map<D, E>>>> map;

    public Map5(
            Function<? super A, ? extends Map<B, Map<C, Map<D, E>>>> map0Creator,
            Function<? super B, ? extends Map<C, Map<D, E>>> map1Creator,
            Function<? super C, ? extends Map<D, E>> map2Creator,
            Map<A, Map<B, Map<C, Map<D, E>>>> map) {
        this.map0Creator = map0Creator;
        this.map1Creator = map1Creator;
        this.map2Creator = map2Creator;
        this.map = map;
    }

    public static <A, B, C, D, E> Map5<A, B, C, D, E> newHashMap() {
        return new Map5<>(
                a -> new HashMap<>(),
                b -> new HashMap<>(),
                c -> new HashMap<>(),
                new HashMap<>()
        );
    }

    public static <A, B, C, D, E> Map5<A, B, C, D, E> newConcurrentHashMap() {
        return new Map5<>(
                a -> new ConcurrentHashMap<>(),
                b -> new ConcurrentHashMap<>(),
                c -> new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }

    public Map<A, Map<B, Map<C, Map<D, E>>>> getMap() {
        return map;
    }

    protected Map<B, Map<C, Map<D, E>>> getMap0(A a) {
        return map.computeIfAbsent(a, map0Creator);
    }

    protected Map<C, Map<D, E>> getMap1(A a, B b) {
        return getMap0(a).computeIfAbsent(b, map1Creator);
    }

    protected Map<D, E> getMap2(A a, B b, C c) {
        return getMap1(a, b).computeIfAbsent(c, map2Creator);
    }

    public E put(A a, B b, C c, D d, E e) {
        return getMap2(a, b, c).put(d, e);
    }

    public E get(A a, B b, C c, D d) {
        Map<B, Map<C, Map<D, E>>> map0 = map.get(a);
        if(map0 == null) return null;
        Map<C, Map<D, E>> map1 = map0.get(b);
        if(map1 == null) return null;
        Map<D, E> map2 = map1.get(c);
        if(map2 == null) return null;
        return map2.get(d);
    }

    public E getOrDefault(A a, B b, C c, D d, E defaultValue) {
        Map<B, Map<C, Map<D, E>>> map0 = map.get(a);
        if(map0 == null) return defaultValue;
        Map<C, Map<D, E>> map1 = map0.get(b);
        if(map1 == null) return defaultValue;
        Map<D, E> map2 = map1.get(c);
        if(map2 == null) return defaultValue;
        return map2.getOrDefault(d, defaultValue);
    }

    public E computeIfAbsent(A a, B b, C c, D d, Function<? super D, ? extends E> func) {
        return getMap2(a, b, c).computeIfAbsent(d, func);
    }

    public E update(A a, B b, C c, D d, E defaultValue, UnaryOperator<E> op) {
        E current = getOrDefault(a, b, c, d, defaultValue);
        E newValue = op.apply(current);
        return put(a, b, c, d, newValue);
    }
}
