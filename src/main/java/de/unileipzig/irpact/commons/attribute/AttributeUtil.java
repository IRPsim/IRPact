package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.ExceptionUtil;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class AttributeUtil {

    public static int getIntValue(Attribute attribute, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute()
                    .getIntValue();
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no int value", name);
        }
    }

    public static void setIntValue(Attribute attribute, int value, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            attribute.asValueAttribute().setIntValue(value);
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no int value", name);
        }
    }

    public static double getDoubleValue(Attribute attribute) {
        return getDoubleValue(attribute, attribute.getName());
    }

    public static double getDoubleValue(Attribute attribute, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute()
                    .getDoubleValue();
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no double value", name);
        }
    }

    public static void setDoubleValue(Attribute attribute, double value) {
        setDoubleValue(attribute, value, attribute.getName());
    }

    public static void setDoubleValue(Attribute attribute, double value, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            attribute.asValueAttribute().setDoubleValue(value);
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no double value", name);
        }
    }

    public static boolean getBooleanValue(Attribute attribute, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute()
                    .getBooleanValue();
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no boolean value", name);
        }
    }

    public static void setBoolean(Attribute attribute, boolean value, String name) {
        if(attribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "attribute '{}' not found", name);
        }
        if(attribute.isValueAttribute()) {
            attribute.asValueAttribute().setBooleanValue(value);
        } else {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' has no boolean value", name);
        }
    }
}
