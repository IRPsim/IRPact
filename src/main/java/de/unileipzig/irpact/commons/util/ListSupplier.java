package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * Persistable supplier for Lists.
 *
 * @author Daniel Abitz
 */
public enum ListSupplier {
    UNKNOWN(-1),
    ARRAY(0),
    LINKED(1);

    private final int ID;

    ListSupplier(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public <E> List<E> newList() {
        switch (ID) {
            case 0:
                return new ArrayList<>();

            case 1:
                return new LinkedList<>();

            default:
                throw new IllegalStateException("unknown");
        }
    }

    public static ListSupplier get(int id) {
        switch (id) {
            case 0:
                return ARRAY;

            case 1:
                return LINKED;

            default:
                return UNKNOWN;
        }
    }

    public static ListSupplier getDefault() {
        return ARRAY;
    }
}
