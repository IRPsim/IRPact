package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class SpatialAttributeBase extends NameableBase implements SpatialAttribute {

    protected final DataType TYPE;
    protected Object value;

    public SpatialAttributeBase(DataType type) {
        this.TYPE = type;
    }

    @Override
    public DataType getDataType() {
        return TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    protected final void checkDouble() {
        if(!isDouble()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isDouble() {
        return TYPE == DataType.DOUBLE;
    }

    @Override
    public double getDoubleValue() {
        checkDouble();
        return value == null ? 0.0 : (double) value;
    }

    @Override
    public void setDoubleValue(double value) {
        checkDouble();
        this.value = value;
    }

    protected final void checkString() {
        if(!isString()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isString() {
        return TYPE == DataType.STRING;
    }

    @Override
    public String getStringValue() {
        checkString();
        return (String) value;
    }

    @Override
    public void setStringValue(String value) {
        checkString();
        this.value = value;
    }
}
