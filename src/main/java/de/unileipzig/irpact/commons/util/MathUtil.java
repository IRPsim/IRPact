package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public final class MathUtil {

    private MathUtil() {
    }

    public static double logistic(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double logit(double x) {
        return Math.log(x / (1.0 - x));
    }
}
