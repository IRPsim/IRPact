package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface ValueAttribute2<V> extends Attribute2 {

    @Override
    ValueAttribute2<V> copy();

    V getValue();

    void setValue(V value);
}
