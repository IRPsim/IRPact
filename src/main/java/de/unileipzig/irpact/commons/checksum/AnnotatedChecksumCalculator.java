package de.unileipzig.irpact.commons.checksum;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class AnnotatedChecksumCalculator extends ChecksumCalculator {

    public static final AnnotatedChecksumCalculator INSTANCE = new AnnotatedChecksumCalculator();

    private final Map<Class<?>, List<Field>> VALID_FIELDS = new WeakHashMap<>();

    protected ChecksumCalculator fallback = Checksums.SMART;

    public AnnotatedChecksumCalculator() {
    }

    public static List<Field> listAllChecksumFields(List<Field> list, Class<?> current) {
        return listAllChecksumFields(list, current, current, Object.class);
    }

    private static List<Field> listAllChecksumFields(List<Field> list, Class<?> current, Class<?> main, Class<?> until) {
        Field[] fields = current.getDeclaredFields();
        for(Field field: fields) {
            if(field.isAnnotationPresent(ChecksumValue.class)) {
                if(current == main) {
                    list.add(field);
                } else {
                    ChecksumValue cv = field.getAnnotation(ChecksumValue.class);
                    if(!cv.declaredOnly()) {
                        list.add(field);
                    }
                }
            }
        }

        if(current.getSuperclass() != null && current.getSuperclass() != until) {
            listAllChecksumFields(list, current.getSuperclass(), main, until);
        }

        return list;
    }

    protected List<Field> getFields(Class<?> c) {
        List<Field> fields = VALID_FIELDS.get(c);
        if(fields == null) {
            fields = listAllChecksumFields(new ArrayList<>(), c);
            VALID_FIELDS.put(c, fields);
        }
        return fields;
    }

    protected int getChecksum(Object obj, Field field) throws IllegalAccessException {
        Object value;

        boolean notAccessible = !field.isAccessible();
        try {
            if(notAccessible) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();
            if(fieldType.isPrimitive()) {
                return getPrimitiveChecksum(obj, field);
            } else {
                value = field.get(obj);
            }
        } finally {
            if(notAccessible) {
                field.setAccessible(false);
            }
        }

        return getChecksum(value);
    }

    protected int getPrimitiveChecksum(Object obj, Field accessibleField) throws IllegalAccessException {
        Class<?> fieldType = accessibleField.getType();
        if(fieldType == int.class) {
            return getChecksum(accessibleField.getInt(obj));
        }
        if(fieldType == long.class) {
            return getChecksum(accessibleField.getLong(obj));
        }
        throw new IllegalStateException("primitive field not supported: " + fieldType);
    }

    protected int getChecksum(Object obj, List<Field> fields) throws IllegalAccessException {
        int cs = 1;
        for(Field field: fields) {
            cs = PRIM * cs + getChecksum(obj, field);
        }
        return cs;
    }

    @Override
    public int getChecksum(Object value) {
        if(value == null) {
            return ChecksumComparable.NULL_CHECKSUM;
        }

        List<Field> fields = getFields(value.getClass());
        if(fields.isEmpty()) {
            return fallback.getChecksum(value);
        } else {
            try {
                return getChecksum(value, fields);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("access exception", e);
            }
        }
    }
}
