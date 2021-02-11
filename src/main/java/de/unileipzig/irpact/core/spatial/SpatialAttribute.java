package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute extends Attribute<Object> {

    DataType getDataType();

    boolean isDouble();

    double getDoubleValue();

    void setDoubleValue(double value);

    boolean isString();

    String getStringValue();

    void setStringValue(String value);
}
