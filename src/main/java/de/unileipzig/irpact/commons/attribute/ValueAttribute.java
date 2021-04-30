package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public interface ValueAttribute extends Attribute {

    //=========================
    //Value
    //=========================

    DataType getDataType();

    Object getValue();

    default <R> R getValue(Class<R> c) {
        return c.cast(getValue());
    }

    @SuppressWarnings("unchecked")
    default <R> R getValueAs() {
        return (R) getValue();
    }

    void setValue(Object value);

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.VALUE;
    }

    @Override
    default ValueAttribute asValueAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================

    default boolean getDoubleValueAsBoolean() {
        return getDoubleValue() == 1.0;
    }

    default void setDoubleValue(boolean value) {
        setDoubleValue(value ? 1.0 : 0.0);
    }

    default int getIntValue() {
        return (int) getDoubleValue();
    }

    default void setIntValue(int value) {
        setDoubleValue(value);
    }

    double getDoubleValue();

    void setDoubleValue(double value);

    String getStringValue();

    void setStringValue(String value);

    default String getValueAsString() {
        switch (getDataType()) {
            case STRING:
                return getStringValue();

            case DOUBLE:
                return Double.toString(getDoubleValue());

            default:
                throw new UnsupportedOperationException("type: " + getDataType());
        }
    }

    UnivariateDoubleDistribution getUnivariateDoubleDistributionValue();

    void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value);
}
