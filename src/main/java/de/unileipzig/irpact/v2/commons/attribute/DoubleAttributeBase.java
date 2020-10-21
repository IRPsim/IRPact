package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class DoubleAttributeBase extends NameableBase implements DoubleAttribute {

    protected double value;

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
