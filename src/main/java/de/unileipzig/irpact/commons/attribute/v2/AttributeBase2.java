package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface AttributeBase2 extends Nameable {

    default <T> T as(Class<T> c) {
        return c.cast(this);
    }

    default boolean is(Class<?> c) {
        return c.isInstance(this);
    }
}
