package de.unileipzig.irpact.jadex.examples.deprecated.old.discrete;

import de.unileipzig.irpact.commons.exception.UncheckedException;

import java.lang.reflect.Field;

public class Ref {

    public static Object getValue(Object value, String field) {
        try {
            Field f = value.getClass().getDeclaredField(field);
            f.setAccessible(true);
            Object v = f.get(value);
            f.setAccessible(false);
            return v;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new UncheckedException(e);
        }
    }
}
