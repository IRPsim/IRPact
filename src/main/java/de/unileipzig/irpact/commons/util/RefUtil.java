package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public final class RefUtil {

    RefUtil() {
    }

    public static boolean isArray(Object obj) {
        return obj.getClass().isArray();
    }

    public static boolean isPrimitiveArray(Object obj) {
        if(obj.getClass().isArray()) {
            return obj.getClass().getComponentType().isPrimitive();
        } else {
            return false;
        }
    }
}
