package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface MapContainerAttribute2<K> extends ContainerAttribute2 {

    @Override
    MapContainerAttribute2<K> copy();

    int size();

    void set(K key, Attribute2 attribute);

    Attribute2 get(K key);

    boolean has(K key);

    boolean remove(K key);
}
