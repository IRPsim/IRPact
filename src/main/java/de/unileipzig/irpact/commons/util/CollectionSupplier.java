package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * Persistable supplier for Lists.
 *
 * @author Daniel Abitz
 */
public enum CollectionSupplier {
    UNKNOWN(-1),
    //list
    ARRAY(0),
    LINKED(1),
    //set
    HASH_SET(2),
    LINKED_HASH_SET(3)
    ;

    private final int ID;

    CollectionSupplier(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public <E> Collection<E> newCollection() {
        switch (ID) {
            case 0:
                return new ArrayList<>();

            case 1:
                return new LinkedList<>();

            case 2:
                return new HashSet<>();

            case 3:
                return new LinkedHashSet<>();

            default:
                throw new IllegalStateException("unknown");
        }
    }

    public static CollectionSupplier get(int id) {
        switch (id) {
            case 0:
                return ARRAY;

            case 1:
                return LINKED;

            case 2:
                return HASH_SET;

            case 3:
                return LINKED_HASH_SET;

            default:
                return UNKNOWN;
        }
    }
}
