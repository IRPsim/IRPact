package de.unileipzig.irpact.core.attribute;

/**
 * @author Daniel Abitz
 */
public interface Attribute extends AttributeBase {

    double getValue();

    Attribute copy();
}