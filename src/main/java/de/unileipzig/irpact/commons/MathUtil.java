package de.unileipzig.irpact.commons;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class MathUtil {

    private MathUtil() {
    }

    public static double nextDouble(Random rnd, double lowerBound, double upperBound) {
        double temp = upperBound - lowerBound;
        return rnd.nextDouble() * temp + lowerBound;
    }
}
