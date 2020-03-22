package de.unileipzig.irpact.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class DoublePair {

    private final double first;
    private final double second;

    public DoublePair(double first, double second) {
        this.first = first;
        this.second = second;
    }

    public static DoublePair get(double first, double second) {
        return new DoublePair(first, second);
    }

    public double first() {
        return first;
    }

    public double second() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof DoublePair) {
            DoublePair other = (DoublePair) obj;
            return first == other.first
                    && second == other.second;
        }
        return false;
    }
}
