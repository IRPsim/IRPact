package de.unileipzig.irpact.commons;

/**
 * @author Daniel Abitz
 */
public final class Util {

    Util() {
    }

    public static int checkNull(Number input, int ifNull) {
        return input == null ? ifNull : input.intValue();
    }

    public static double checkNull(Number input, double ifNull) {
        return input == null ? ifNull : input.doubleValue();
    }

    public static <T> T checkNull(T input, T ifNull) {
        return input == null ? ifNull : input;
    }
}
