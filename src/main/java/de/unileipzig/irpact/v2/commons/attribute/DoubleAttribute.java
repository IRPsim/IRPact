package de.unileipzig.irpact.v2.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute extends Attribute<Number> {

    default Double getValue() {
        return getDoubleValue();
    }

    double getDoubleValue();
}
