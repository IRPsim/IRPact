package de.unileipzig.irpact.commons;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class Util {

    public static final long USE_RANDOM_SEED = 0L;
    public static final Random RND = new Random();

    private Util() {
    }

    public static long getRandomSeedIf0(long seed) {
        return seed == USE_RANDOM_SEED ? RND.nextLong() : seed;
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
