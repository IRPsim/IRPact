package de.unileipzig.irpact.input.old.v2;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class RefTest {

    private static boolean isPrimitiveOrStringOrBoxed(Class<?> c) {
        return c.isPrimitive()
                || c == String.class
                || c == Byte.class
                || c == Short.class
                || c == Integer.class
                || c == Long.class
                || c == Float.class
                || c == Double.class
                || c == Character.class
                || c == Boolean.class;
    }

    private static boolean isList(Class<?> c) {
        return c == List.class;
    }

    private static boolean isMap(Class<?> c) {
        return c == Map.class;
    }

    public static void main(String[] args) {
        Class<?> c = ProductGroupAttribute.class;
        for(Field field: c.getDeclaredFields()) {
            System.out.println(field.getType()
                    + " " + isPrimitiveOrStringOrBoxed(field.getType())
                    + " " + isList(field.getType())
                    + " " + isMap(field.getType())
                    + " " + field.getName()
            );
        }
    }
}
