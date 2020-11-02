package de.unileipzig.irpact.v2.commons;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class Util {

    public static final Random RND = new Random();

    private Util() {
    }

    public static String printClass(Object obj) {
        return obj == null
                ? "null"
                : obj.getClass().toString();
    }

    public static int nextInt(Random rnd, int lower, int upper) {
        return rnd.nextInt(upper - lower) + lower;
    }

    public static double nextDouble(Random rnd, double lower, double upper) {
        return rnd.nextDouble() * (upper - lower) + lower;
    }
}
