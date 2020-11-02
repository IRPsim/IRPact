package de.unileipzig.irpact.v2.jadex.util;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public final class AttributeUtil {

    private AttributeUtil() {
    }

    public static boolean isTrue(DoubleAttribute attribute) {
        return attribute.getValue() == 1.0;
    }

    public static boolean isFalse(DoubleAttribute attribute) {
        return attribute.getValue() != 1.0;
    }
}
