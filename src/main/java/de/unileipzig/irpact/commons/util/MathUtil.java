package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public final class MathUtil {

    private MathUtil() {
    }

    public static int rndSign(Rnd rnd) {
        return rnd.nextBoolean() ? 1 : -1;
    }

    public static double logistic(double x) {
        return logistic(1.0, x);
    }

    public static double logistic(double L, double x) {
        return L / (1.0 + Math.exp(-x));
    }

    public static double logistic(double L, double k, double x, double x0) {
        return L / (1.0 + Math.exp(-k * (x - x0)));
    }

    public static double logit(double x) {
        return Math.log(x / (1.0 - x));
    }

    public static double transform(double in, double inMin, double inMax, double outMin, double outMax) {
        double slope = (outMax - outMin) / (inMax - inMin);
        return outMin + slope * (in - inMin);
    }
}
