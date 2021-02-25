package de.unileipzig.irpact.commons.attribute;

import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public final class AttributeUtil {

    private AttributeUtil() {
    }

    public static double getDoubleValue(Attribute<?> attr, Supplier<String> errMsg) throws IllegalArgumentException {
        if(attr instanceof DoubleAttribute) {
            return ((DoubleAttribute) attr).getDoubleValue();
        } else {
            Object value = attr.getValue();
            if(value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                String msg = errMsg == null
                        ? "no double"
                        : errMsg.get();
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
