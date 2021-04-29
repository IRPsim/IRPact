package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.develop.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface Attribute extends AttributeBase {

    default Attribute copyAttribute() {
        throw new UnsupportedOperationException();
    }

    default <R> R as(Class<R> c) {
        return c.cast(this);
    }

    DataType getType();

    default <R> R getValue(Class<R> c) {
        return c.cast(getValue());
    }

    @SuppressWarnings("unchecked")
    default <R> R getValueAs() {
        return (R) getValue();
    }

    Object getValue();

    void setValue(Object value);

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

    default boolean isStringValue(String input) {
        return Objects.equals(getStringValue(), input);
    }

    String getStringValue();

    void setStringValue(String value);

    default String getValueAsString() {
        switch (getType()) {
            case STRING:
                return getStringValue();

            case DOUBLE:
                return Double.toString(getDoubleValue());

            default:
                throw new UnsupportedOperationException("type: " + getType());
        }
    }

    UnivariateDoubleDistribution getUnivariateDoubleDistributionValue();

    void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value);
}
