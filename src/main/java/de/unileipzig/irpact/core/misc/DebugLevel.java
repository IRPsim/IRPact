package de.unileipzig.irpact.core.misc;

/**
 * @author Daniel Abitz
 */
public enum DebugLevel {
    /**
     * Special Level for invalid Ids.
     */
    UNKNOWN(-1),
    /**
     * Print normal Informations.
     */
    DEFAULT(0),
    /**
     * Enables debug Informations.
     */
    DEBUG(1),
    /**
     * Enables trace Informations.
     */
    TRACE(2);

    public static final String DEFAULT_ID = "0";

    private final int ID;

    DebugLevel(int id) {
        ID = id;
    }

    public int id() {
        return ID;
    }

    public boolean isValid() {
        return this != UNKNOWN;
    }

    public static DebugLevel get(int id) {
        for(DebugLevel level: values()) {
            if(level.id() == id) {
                return level;
            }
        }
        return UNKNOWN;
    }

    public static DebugLevel defaultIfUnknown(DebugLevel level) {
        return level == UNKNOWN ? DEBUG : level;
    }
}
