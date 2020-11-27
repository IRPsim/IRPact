package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute extends Attribute<Number> {

    default Double getValue() {
        return getDoubleValue();
    }

    double getDoubleValue();
}
