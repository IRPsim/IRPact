package de.unileipzig.irpact.core.process2.modular;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
public class MapBasedSharedModuleData implements SharedModuleData {

    protected final Lock LOCK = new ReentrantLock();

    protected MapSupplier mapSupplier;
    protected Map<Object, Object> keyValueMapping;
    protected Map<Object, Map<Object, Object>> keyKeyValueMapping;

    public MapBasedSharedModuleData() {
        this(MapSupplier.CONCURRENT_HASH);
    }

    public MapBasedSharedModuleData(MapSupplier mapSupplier) {
        this.mapSupplier = mapSupplier;
        keyValueMapping = mapSupplier.newMap();
        keyKeyValueMapping = mapSupplier.newMap();
    }

    //=========================
    //general
    //=========================

    @Override
    public void lock() {
        LOCK.lock();
    }

    @Override
    public void unlock() {
        LOCK.unlock();
    }

    @Override
    public Lock getLock() {
        return LOCK;
    }

    @Override
    public void removeAll() {
        keyValueMapping.clear();
    }

    //=========================
    //key-value store
    //=========================

    @Override
    public boolean contains(Object key) {
        return keyValueMapping.containsKey(key);
    }

    @Override
    public Object put(Object key, Object obj) {
        return keyValueMapping.put(key, obj);
    }

    @Override
    public boolean remove(Object key) {
        if(keyValueMapping.containsKey(key)) {
            keyValueMapping.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object get(Object key) {
        return keyValueMapping.get(key);
    }

    //=========================
    //key-key-value store
    //=========================

    @Override
    public boolean contains(Object year, Object key) {
        Map<Object, Object> map = keyKeyValueMapping.get(year);
        return map != null && map.containsKey(key);
    }

    @Override
    public void put(Object year, Object key, Object obj) {
        keyKeyValueMapping.computeIfAbsent(year, _year -> mapSupplier.newMap()).put(key, obj);
    }

    @Override
    public boolean remove(Object year, Object key) {
        Map<Object, Object> map = keyKeyValueMapping.get(year);
        return map != null && map.remove(key) != null;
    }

    @Override
    public Object get(Object year, Object key) {
        Map<Object, Object> map = keyKeyValueMapping.get(year);
        return map == null
                ? null
                : map.get(key);
    }
}
