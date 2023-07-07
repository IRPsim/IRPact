package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.commons.util.SetSupplier;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
public class MapDataStore extends NameableBase implements DataStore {

    protected final Lock LOCK = new ReentrantLock();
    protected MapSupplier mapSupplier;
    protected SetSupplier setSupplier;
    protected Map<Object, Object> data;
    protected Map<Object, Map<Object, Object>> annualData;
    protected Map<Object, Set<Object>> flags;

    public MapDataStore() {
        this(MapSupplier.LINKED, SetSupplier.LINKED);
    }

    public MapDataStore(MapSupplier mapSupplier, SetSupplier setSupplier) {
        this(mapSupplier, setSupplier, mapSupplier.newMap(), mapSupplier.newMap(), mapSupplier.newMap());
    }

    public MapDataStore(
            MapSupplier mapSupplier,
            SetSupplier setSupplier,
            Map<Object, Object> data,
            Map<Object, Map<Object, Object>> annualData,
            Map<Object, Set<Object>> flags) {
        this.mapSupplier = mapSupplier;
        this.setSupplier = setSupplier;
        this.data = data;
        this.annualData = annualData;
        this.flags = flags;
    }

    protected MapDataStore(MapDataStore other) {
        this.mapSupplier = other.mapSupplier;
        this.data = mapSupplier.newMap();
        this.annualData = mapSupplier.newMap();
        this.flags = mapSupplier.newMap();

        data.putAll(other.data);
        for(Map.Entry<Object, Map<Object, Object>> entry: other.annualData.entrySet()) {
            Map<Object, Object> map = mapSupplier.newMap();
            map.putAll(entry.getValue());
            annualData.put(entry.getKey(), map);
        }
        for(Map.Entry<Object, Set<Object>> entry: other.flags.entrySet()) {
            Set<Object> set = setSupplier.newSet();
            set.addAll(entry.getValue());
            flags.put(entry.getKey(), set);
        }
    }

    public MapDataStore copy() {
        return new MapDataStore(this);
    }

    protected static void validateKey(Object key) {
        if(key == null) throw new NullPointerException("key is null");
    }

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
        data.clear();
    }

    //=========================
    //key
    //=========================

    @Override
    public boolean contains(Object key) {
        return key != null && data.containsKey(key);
    }

    @Override
    public void put(Object key, Object obj) {
        validateKey(key);
        data.put(key, obj);
    }

    @Override
    public boolean remove(Object key) {
        return key != null && data.remove(key) != null;
    }

    @Override
    public Object get(Object key) {
        validateKey(key);
        return data.get(key);
    }

    //=========================
    //annual key
    //=========================

    @Override
    public boolean contains(Object year, Object key) {
        if(key == null) return false;
        Map<Object, Object> map = annualData.get(year);
        return map != null && map.containsKey(key);
    }

    @Override
    public void put(Object year, Object key, Object obj) {
        annualData.computeIfAbsent(year, _year -> mapSupplier.newMap()).put(key, obj);
    }

    @Override
    public boolean remove(Object year, Object key) {
        if(key == null) return false;
        Map<Object, Object> map = annualData.get(year);
        return map != null && map.remove(key) != null;
    }

    @Override
    public Object get(Object year, Object key) {
        validateKey(key);
        Map<Object, Object> map = annualData.get(year);
        return map == null
                ? null
                : map.get(key);
    }

    //=========================
    //flag
    //=========================

    @Override
    public boolean hasFlag(Object key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.contains(flag);
    }

    @Override
    public void setFlag(Object key, Object flag) {
        validateKey(key);
        flags.computeIfAbsent(key, _key -> setSupplier.newSet()).add(flag);
    }

    @Override
    public boolean removeFlag(Object key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.remove(flag);
    }
}
