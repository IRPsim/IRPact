package de.unileipzig.irpact.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Util {

    private static final int PRIM = 31;

    private Util() {
    }

    //um boxing + varargs zu vermeiden
    public static int hash(double d1, double d2) {
        int result = 1;
        result = PRIM * result + Double.hashCode(d1);
        result = PRIM * result + Double.hashCode(d2);
        return result;
    }

    //um varargs zu vermeiden - cursed micro optimization
    public static int hash(Object o1, Object o2) {
        int result = 1;
        result = PRIM * result + Objects.hashCode(o1);
        result = PRIM * result + Objects.hashCode(o2);
        return result;
    }

    //um varargs zu vermeiden - cursed micro optimization
    public static int hash(Object o1, Object o2, Object o3) {
        int result = 1;
        result = PRIM * result + Objects.hashCode(o1);
        result = PRIM * result + Objects.hashCode(o2);
        result = PRIM * result + Objects.hashCode(o3);
        return result;
    }

    public static int hash(Object o1, double d2) {
        int result = 1;
        result = PRIM * result + Objects.hashCode(o1);
        result = PRIM * result + Double.hashCode(d2);
        return result;
    }

    public static int hash(Object o1, long l2) {
        int result = 1;
        result = PRIM * result + Objects.hashCode(o1);
        result = PRIM * result + Long.hashCode(l2);
        return result;
    }
}
