package de.unileipzig.irpact.commons;

/**
 * For equals-checks without using {@link #equals(Object)}.
 *
 * @author Daniel Abitz
 */
public interface IsEquals {

    default boolean isEquals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(obj.getClass() == getClass()) return isEqualsSameClass(obj);
        return false;
    }

    default boolean isEqualsSameClass(Object obj) {
        return obj == this;
    }
}
