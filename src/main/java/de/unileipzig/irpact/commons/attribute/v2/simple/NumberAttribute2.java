package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface NumberAttribute2 extends ValueAttribute2<Number> {

    @Override
    NumberAttribute2 copy();

    boolean getBoolean();
    void setBoolean(boolean value);

    int getInt();
    void setInt(int value);

    long getLong();
    void setLong(long value);

    double getDouble();
    void setDouble(double value);
}
