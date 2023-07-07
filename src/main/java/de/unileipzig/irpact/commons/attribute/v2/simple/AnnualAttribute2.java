package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface AnnualAttribute2 extends MapContainerAttribute2<Integer> {

    @Override
    AnnualAttribute2 copy();

    @Override
    default AttributeType2 getType() {
        return AttributeType2.ANNUAL;
    }

    @Override
    default boolean isAnnual() {
        return true;
    }
    @Override
    default AnnualAttribute2 asAnnual() {
        return this;
    }

    Attribute2 get(int year);

    boolean has(int year);

    boolean remove(int year);
}
