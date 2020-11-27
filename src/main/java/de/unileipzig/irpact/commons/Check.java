package de.unileipzig.irpact.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Check {

    private Check() {
    }

    //damit alles auf check zusammen laeuft
    public static <T> T requireNonNull(T value) {
        return Objects.requireNonNull(value);
    }

    public static double checkRangeInclusive(double value, double lower, double upper) {
        if(value < lower || value > upper) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static double checkRangeExclusive(double value, double lower, double upper) {
        if(value < lower || value >= upper) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static <T> T requireNonNull(T value, String msg) {
        //damit alles auf check zusammen laeuft
        return Objects.requireNonNull(value, msg);
    }

    public static double require0to1(double value, String msg) {
        if(value < 0 || value > 0) {
            throw new IllegalArgumentException(msg);
        }
        return value;
    }

    public static double checkFirstSmallerEquals(double value0, double value1, String msg) {
        if(value0 <= value1) {
            return value0;
        }
        throw new IllegalArgumentException(msg);
    }

    public static double checkFirstLargerEquals(double value0, double value1, String msg) {
        if(value0 >= value1) {
            return value0;
        }
        throw new IllegalArgumentException(msg);
    }
}
