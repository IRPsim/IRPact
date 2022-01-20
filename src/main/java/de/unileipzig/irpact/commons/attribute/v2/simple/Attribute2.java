package de.unileipzig.irpact.commons.attribute.v2.simple;

import de.unileipzig.irpact.commons.attribute.v2.AttributeBase2;

/**
 * @author Daniel Abitz
 */
public interface Attribute2 extends AttributeBase2 {

    Attribute2 copy();

    AttributeType2 getType();

    boolean isArtificial();

    default boolean isDouble() {
        return false;
    }
    default DoubleAttribute2 asDouble() {
        throw new UnsupportedOperationException("not supported (" + getClass().getSimpleName() + ")");
    }

    default boolean isString() {
        return false;
    }
    default StringAttribute2 asString() {
        throw new UnsupportedOperationException("not supported (" + getClass().getSimpleName() + ")");
    }

    default boolean isAnnual() {
        return false;
    }
    default AnnualAttribute2 asAnnual() {
        throw new UnsupportedOperationException("not supported (" + getClass().getSimpleName() + ")");
    }

    default boolean isProduct() {
        return false;
    }
    default ProductRelatedAttribute2 asProduct() {
        throw new UnsupportedOperationException("not supported (" + getClass().getSimpleName() + ")");
    }
}
