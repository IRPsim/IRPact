package de.unileipzig.irpact.commons.attribute.v2.simple;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractMapContainerAttribute2<K>
        extends AbstractContainerAttribute2
        implements MapContainerAttribute2<K> {

    protected abstract Map<K, Attribute2> map();

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public void set(K key, Attribute2 attribute) {
        map().put(key, attribute);
    }

    @Override
    public Attribute2 get(K key) {
        return map().get(key);
    }

    @Override
    public boolean has(K key) {
        return map().containsKey(key);
    }

    @Override
    public boolean remove(K key) {
        if(has(key)) {
            map().remove(key);
            return true;
        } else {
            return false;
        }
    }
}
