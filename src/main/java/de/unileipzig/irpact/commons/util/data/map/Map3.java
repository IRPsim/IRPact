package de.unileipzig.irpact.commons.util.data.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class Map3<A, B, C> {

    protected Function<? super A, ? extends Map<B, C>> map0Creator;
    protected Map<A, Map<B, C>> map;

    public Map3(
            Function<? super A, ? extends Map<B, C>> map0Creator,
            Map<A, Map<B, C>> map) {
        this.map0Creator = map0Creator;
        this.map = map;
    }

    public static <A, B, C> Map3<A, B, C> newHashMap() {
        return new Map3<>(
                a -> new HashMap<>(),
                new HashMap<>()
        );
    }

    public static <A, B, C> Map3<A, B, C> newLinkedHashMap() {
        return new Map3<>(
                a -> new LinkedHashMap<>(),
                new LinkedHashMap<>()
        );
    }

    public static <A, B, C> Map3<A, B, C> newConcurrentHashMap() {
        return new Map3<>(
                a -> new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }

    public Map<A, Map<B, C>> getMap() {
        return map;
    }

    protected Map<B, C> getMap0(A a) {
        return map.computeIfAbsent(a, map0Creator);
    }

    public void clear() {
        map.clear();
    }

    public void clear(A a) {
        Map<B, C> map0 = getMap0(a);
        if(map0 != null) {
            map0.clear();
        }
    }

    public C put(A a, B b, C c) {
        return getMap0(a).put(b, c);
    }

    public void putAll(A a, Map<? extends B, ? extends C> m) {
        getMap0(a).putAll(m);
    }

    public Map<B, C> get(A a) {
        return map.get(a);
    }

    public C get(A a, B b) {
        Map<B, C> map0 = map.get(a);
        if(map0 == null) return null;
        return map0.get(b);
    }

    public boolean contains(A a) {
        return map.containsKey(a);
    }

    public boolean contains(A a, B b) {
        Map<B, C> map0 = map.get(a);
        return map0 != null && map0.containsKey(b);
    }

    public Map<B, C> remove(A a) {
        return map.remove(a);
    }

    public C remove(A a, B b) {
        Map<B, C> map0 = map.get(a);
        if(map0 == null) return null;
        return map0.remove(b);
    }

    public C getOrDefault(A a, B b, C defaultValue) {
        Map<B, C> map0 = map.get(a);
        if(map0 == null) return defaultValue;
        return map0.getOrDefault(b, defaultValue);
    }

    public C update(A a, B b, C defaultValue, UnaryOperator<C> op) {
        C current = getOrDefault(a, b, defaultValue);
        C newValue = op.apply(current);
        return put(a, b, newValue);
    }

    public boolean isEmpty(A a) {
        Map<B, C> map0 = map.get(a);
        return map0 == null || map0.isEmpty();
    }

    public Collection<Map<B, C>> values() {
        return map.values();
    }

    public Set<A> keySet() {
        return map.keySet();
    }

    public Set<B> keySet(A a) {
        Map<B, C> map0 = map.get(a);
        return map0 == null
                ? Collections.emptySet()
                : map0.keySet();
    }

    public boolean getAllKeysSetsB(Set<B> set) {
        boolean changed = false;
        for(A a: keySet()) {
            Set<B> bset = keySet(a);
            changed |= set.addAll(bset);
        }
        return changed;
    }

    public Set<Map.Entry<B, C>> entrySet(A a) {
        Map<B, C> map0 = map.get(a);
        return map0 == null
                ? Collections.emptySet()
                : map0.entrySet();
    }

    public Stream<A> streamKeys() {
        return map.keySet().stream();
    }

    public Stream<B> streamKeys(A a) {
        Map<B, C> map0 = map.get(a);
        return map0 == null
                ? Stream.empty()
                : map0.keySet().stream();
    }
}
