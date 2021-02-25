package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.DataType;

/**
 * @author Daniel Abitz
 */
public interface Attribute<T> extends AttributeBase {

    default Attribute<T> copyAttribute() {
        throw new UnsupportedOperationException();
    }

    T getValue();

    void setValue(T value);

    DataType getType();

    default <R> R as(Class<R> c) {
        return c.cast(this);
    }
}
