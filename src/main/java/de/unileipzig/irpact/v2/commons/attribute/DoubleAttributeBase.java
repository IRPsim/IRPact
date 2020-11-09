package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class DoubleAttributeBase extends NameableBase implements DoubleAttribute {

    protected double value;

    public void setValue(Number value) {
        setDoubleValue(value.doubleValue());
    }

    public void setDoubleValue(double value) {
        this.value = value;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }
}
