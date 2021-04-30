package de.unileipzig.irpact.commons.attribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public enum AttributeType {
    VALUE,
    ANNUAL,
    RELATED,
    GROUP;

    public static Set<AttributeType> setOf(AttributeType first, AttributeType... more) {
        EnumSet<AttributeType> set = EnumSet.of(first, more);
        return Collections.unmodifiableSet(set);
    }
}
