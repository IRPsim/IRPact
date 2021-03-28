package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public enum DataType {
    UNKNOWN(0),
    OTHER(1),
    DOUBLE(2),
    STRING(3),
    UNIVARIATE_DOUBLE_DISTRIBUTION(4)
    ;

    private final int ID;

    DataType(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public static DataType get(int id) {
        for(DataType type: values()) {
            if(type.getID() == id) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
