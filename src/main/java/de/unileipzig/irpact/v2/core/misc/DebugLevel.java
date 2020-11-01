package de.unileipzig.irpact.v2.core.misc;

/**
 * @author Daniel Abitz
 */
public enum DebugLevel {

    ;

    private final int ID;

    DebugLevel(int id) {
        ID = id;
    }

    public int id() {
        return ID;
    }
}
