package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class DoubleAttributeBase extends NameableBase implements DoubleAttribute {

    protected double value;

    public void setValue(Number value) {
        setDoubleValue(value.doubleValue());
    }

    @Override
    public void setDoubleValue(double value) {
        this.value = value;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }
}
