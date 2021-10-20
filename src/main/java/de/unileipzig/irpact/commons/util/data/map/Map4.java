package de.unileipzig.irpact.commons.util.data.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class Map4<A, B, C, D> {

    protected Function<? super A, ? extends Map<B, Map<C, D>>> map0Creator;
    protected Function<? super B, ? extends Map<C, D>> map1Creator;
    protected Map<A, Map<B, Map<C, D>>> map;

    public Map4(
            Function<? super A, ? extends Map<B, Map<C, D>>> map0Creator,
            Function<? super B, ? extends Map<C, D>> map1Creator,
            Map<A, Map<B, Map<C, D>>> map) {
        this.map0Creator = map0Creator;
        this.map1Creator = map1Creator;
        this.map = map;
    }

    public static <A, B, C, D> Map4<A, B, C, D> newHashMap() {
        return new Map4<>(
                a -> new HashMap<>(),
                b -> new HashMap<>(),
                new HashMap<>()
        );
    }

    public static <A, B, C, D> Map4<A, B, C, D> newConcurrentHashMap() {
        return new Map4<>(
                a -> new ConcurrentHashMap<>(),
                b -> new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }

    public Map<A, Map<B, Map<C, D>>> getMap() {
        return map;
    }

    protected Map<B, Map<C, D>> getMap0(A a) {
        return map.computeIfAbsent(a, map0Creator);
    }

    protected Map<C, D> getMap1(A a, B b) {
        return getMap0(a).computeIfAbsent(b, map1Creator);
    }

    public D put(A a, B b, C c, D d) {
        return getMap1(a, b).put(c, d);
    }

    public D get(A a, B b, C c) {
        Map<B, Map<C, D>> map0 = map.get(a);
        if(map0 == null) return null;
        Map<C, D> map1 = map0.get(b);
        if(map1 == null) return null;
        return map1.get(c);
    }

    public D getOrDefault(A a, B b, C c, D defaultValue) {
        Map<B, Map<C, D>> map0 = map.get(a);
        if(map0 == null) return defaultValue;
        Map<C, D> map1 = map0.get(b);
        if(map1 == null) return defaultValue;
        return map1.getOrDefault(c, defaultValue);
    }

    public D update(A a, B b, C c, D defaultValue, UnaryOperator<D> op) {
        D current = getOrDefault(a, b, c, defaultValue);
        D newValue = op.apply(current);
        return put(a, b, c, newValue);
    }

    public D computeIfAbsent(A a, B b, C c, Function<? super C, ? extends D> func) {
        return getMap1(a, b).computeIfAbsent(c, func);
    }
}
