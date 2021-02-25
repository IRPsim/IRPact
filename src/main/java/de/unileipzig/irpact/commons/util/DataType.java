package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public enum DataType {
    UNKNOWN(-1),
    DOUBLE(0),
    STRING(1),
    OTHER(2);

    private final int ID;

    DataType(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public static DataType get(int id) {
        switch (id) {
            case 0:
                return DOUBLE;

            case 1:
                return STRING;

            case 2:
                return OTHER;

            default:
                throw new IllegalArgumentException("unknown id: " + id);
        }
    }
}
