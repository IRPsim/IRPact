package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * Persistable supplier for maps.
 *
 * @author Daniel Abitz
 */
public enum MapSupplier {
    UNKNOWN(-1),
    HASH(0),
    LINKED(1),
    TREE(2);

    private final int ID;

    MapSupplier(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public <K, V> Map<K, V> newMap() {
        return newMapWithParam(null);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> newMapWithParam(Object param) {
        switch (ID) {
            case 0:
                return new HashMap<>();

            case 1:
                return new LinkedHashMap<>();

            case 2:
                Comparator<K> c = (Comparator<K>) param;
                return new TreeMap<>(c);

            default:
                throw new IllegalStateException("unknown");
        }
    }

    public <K, V> Map<K, V> copyMap(Map<? extends K, ? extends V> m) {
        return copyMapWithParam(null, m);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> copyMapWithParam(Object param, Map<? extends K, ? extends V> m) {
        switch (ID) {
            case 0:
                return new HashMap<>(m);

            case 1:
                return new LinkedHashMap<>(m);

            case 2:
                Comparator<K> c = (Comparator<K>) param;
                TreeMap<K, V> tm = new TreeMap<>(c);
                tm.putAll(m);
                return tm;

            default:
                throw new IllegalStateException("unknown");
        }
    }

    public static MapSupplier get(int id) {
        switch (id) {
            case 0:
                return HASH;

            case 1:
                return LINKED;

            case 2:
                return TREE;

            default:
                return UNKNOWN;
        }
    }

    public static MapSupplier getDefault() {
        return LINKED;
    }
}
