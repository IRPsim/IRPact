package de.unileipzig.irpact.commons.util.data;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

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

    public static final Set<DataType> SET_DOUBLE = asReadOnlySet(DOUBLE);
    public static final Set<DataType> SET_STRING = asReadOnlySet(STRING);
    public static final Set<DataType> SET_UNIVARIATE_DOUBLE_DISTRIBUTION = asReadOnlySet(UNIVARIATE_DOUBLE_DISTRIBUTION);

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

    public static EnumSet<DataType> asEnumSet(DataType first, DataType... more) {
        return EnumSet.of(first, more);
    }

    public static Set<DataType> asReadOnlySet(DataType first, DataType... more) {
        return Collections.unmodifiableSet(asEnumSet(first, more));
    }
}
