package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public final class Utils {

    private Utils() {
    }

    public static String printClass(Object obj) {
        if(obj == null) {
            return "null";
        }
        if(obj instanceof Class<?>) {
            return obj.toString();
        }
        return obj.getClass().toString();
    }

    public static String printSimpleClassName(Object obj) {
        if(obj == null) {
            return "null";
        }
        if(obj instanceof Class<?>) {
            return ((Class<?>) obj).getSimpleName();
        }
        return obj.getClass().getSimpleName();
    }
}
