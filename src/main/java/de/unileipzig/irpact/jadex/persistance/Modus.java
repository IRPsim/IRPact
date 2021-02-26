package de.unileipzig.irpact.jadex.persistance;

/**
 * @author Daniel Abitz
 */
public enum Modus {
    BINARY,
    PARAMETER;

    public static Modus getDefault() {
        return BINARY;
    }
}
