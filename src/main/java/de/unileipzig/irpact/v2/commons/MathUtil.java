package de.unileipzig.irpact.v2.commons;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class MathUtil {

    private MathUtil() {
    }

    public static double nextDouble(Random rnd, double lowerBound, double upperBound) {
        return rnd.nextDouble() * (upperBound - lowerBound) + lowerBound;
    }
}
