package de.unileipzig.irpact.commons.attribute.v3;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface AttributeBase extends Nameable {

    default <T> T as(Class<T> c) {
        return c.cast(this);
    }

    default boolean is(Class<?> c) {
        return c.isInstance(this);
    }

    default AttributeBase copy() {
        throw new UnsupportedOperationException();
    }

    boolean isArtificial();
}
