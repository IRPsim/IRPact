package de.unileipzig.irpact.v2.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Util {

    private Util() {
    }

    public static String printClass(Object obj) {
        return obj == null
                ? "null"
                : obj.getClass().toString();
    }

    public static int hash(double x0, double x1) {
        return Objects.hash(x0, x1);
    }
}
