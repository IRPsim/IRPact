package de.unileipzig.irpact.core.preference;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class BasicValue implements Value {

    private String value;

    public BasicValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof BasicValue) {
            BasicValue other = (BasicValue) obj;
            return Objects.equals(value, other.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
