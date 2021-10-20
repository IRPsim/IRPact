package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * Persistable supplier for sets.
 *
 * @author Daniel Abitz
 */
public enum SetSupplier {
    UNKNOWN(-1),
    HASH(0),
    LINKED(1),
    CONCURRENT_HASH(2);

    private final int ID;

    SetSupplier(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public <E> Set<E> newSet() {
        switch (ID) {
            case 0:
                return new HashSet<>();

            case 1:
                return new LinkedHashSet<>();

            case 2:
                return Collections.synchronizedSet(new HashSet<>());

            default:
                throw new IllegalStateException("unknown");
        }
    }

    public static SetSupplier get(int id) {
        switch (id) {
            case 0:
                return HASH;

            case 1:
                return LINKED;

            case 2:
                return CONCURRENT_HASH;

            default:
                return UNKNOWN;
        }
    }

    public static SetSupplier getDefault() {
        return LINKED;
    }
}
