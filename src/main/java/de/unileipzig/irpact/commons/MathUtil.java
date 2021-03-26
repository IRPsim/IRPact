package de.unileipzig.irpact.commons;

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

    public static double logistic(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double logit(double x) {
        return Math.log(x / (1.0 - x));
    }
}
