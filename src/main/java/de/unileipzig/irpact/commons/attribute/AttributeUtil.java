package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;

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

    //==================================================
    //simplified annual
    //==================================================

    public static int getInt(Attribute attribute, String name, int year) {
        if(attribute == null) {
            throw new NoSuchElementException(StringUtil.format("attribute '{}' not found", name));
        }

        if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(year);
            if(attr == null) {
                throw new NoSuchElementException(StringUtil.format("annual attribute '{}' for year '{}' not found", name, year));
            }
            else if(attr.isValueAttribute()) {
                return attr.asValueAttribute().getIntValue(); //!
            } else {
                throw new IllegalArgumentException(StringUtil.format("annual attribute '{}' for year '{}' has no int: {}", name, year, attr.getClass().getSimpleName()));
            }
        } else if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute().getIntValue(); //!
        } else {
            throw new IllegalArgumentException(StringUtil.format("attribute '{}' has no int: {}", name, year, attribute.getClass().getSimpleName()));
        }
    }

    public static long getLong(Attribute attribute, String name, int year) {
        if(attribute == null) {
            throw new NoSuchElementException(StringUtil.format("attribute '{}' not found", name));
        }

        if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(year);
            if(attr == null) {
                throw new NoSuchElementException(StringUtil.format("annual attribute '{}' for year '{}' not found", name, year));
            }
            else if(attr.isValueAttribute()) {
                return attr.asValueAttribute().getLongValue(); //!
            } else {
                throw new IllegalArgumentException(StringUtil.format("annual attribute '{}' for year '{}' has no int: {}", name, year, attr.getClass().getSimpleName()));
            }
        } else if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute().getLongValue(); //!
        } else {
            throw new IllegalArgumentException(StringUtil.format("attribute '{}' has no int: {}", name, year, attribute.getClass().getSimpleName()));
        }
    }

    public static double getDouble(Attribute attribute, String name, int year) {
        if(attribute == null) {
            throw new NoSuchElementException(StringUtil.format("attribute '{}' not found", name));
        }

        if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(year);
            if(attr == null) {
                throw new NoSuchElementException(StringUtil.format("annual attribute '{}' for year '{}' not found", name, year));
            }
            else if(attr.isValueAttribute()) {
                return attr.asValueAttribute().getDoubleValue(); //!
            } else {
                throw new IllegalArgumentException(StringUtil.format("annual attribute '{}' for year '{}' has no int: {}", name, year, attr.getClass().getSimpleName()));
            }
        } else if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute().getDoubleValue(); //!
        } else {
            throw new IllegalArgumentException(StringUtil.format("attribute '{}' has no int: {}", name, year, attribute.getClass().getSimpleName()));
        }
    }

    public static String getString(Attribute attribute, String name, int year) {
        if(attribute == null) {
            throw new NoSuchElementException(StringUtil.format("attribute '{}' not found", name));
        }

        if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(year);
            if(attr == null) {
                throw new NoSuchElementException(StringUtil.format("annual attribute '{}' for year '{}' not found", name, year));
            }
            else if(attr.isValueAttribute()) {
                return attr.asValueAttribute().getStringValue(); //!
            } else {
                throw new IllegalArgumentException(StringUtil.format("annual attribute '{}' for year '{}' has no int: {}", name, year, attr.getClass().getSimpleName()));
            }
        } else if(attribute.isValueAttribute()) {
            return attribute.asValueAttribute().getStringValue(); //!
        } else {
            throw new IllegalArgumentException(StringUtil.format("attribute '{}' has no int: {}", name, year, attribute.getClass().getSimpleName()));
        }
    }
}
