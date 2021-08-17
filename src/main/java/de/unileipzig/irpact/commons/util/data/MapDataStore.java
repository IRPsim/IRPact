package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class MapDataStore extends NameableBase implements DataStore {

    private static final Function<Object, Set<Object>> SET_CREATOR = key -> new LinkedHashSet<>();

    protected final Lock LOCK = new ReentrantLock();
    protected MapSupplier supplier;
    protected Map<Object, Object> data;
    protected Map<Object, Set<Object>> flags;

    public MapDataStore() {
        this(MapSupplier.LINKED);
    }

    public MapDataStore(MapSupplier supplier) {
        this(supplier, supplier.newMap());
    }

    public MapDataStore(MapSupplier supplier, Map<Object, Object> data) {
        this.supplier = supplier;
        this.data = data;
        this.flags = supplier.newMap();
    }

    protected MapDataStore(MapDataStore other) {
        this.supplier = other.supplier;
        this.data = supplier.newMap();
        this.flags = supplier.newMap();

        data.putAll(other.data);
        for(Map.Entry<Object, Set<Object>> entry: other.flags.entrySet()) {
            Set<Object> set = SET_CREATOR.apply(entry.getKey());
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
    public void removeAll() {
        data.clear();
    }

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

    @Override
    public boolean hasFlag(Object key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.contains(flag);
    }

    @Override
    public void setFlag(Object key, Object flag) {
        validateKey(key);
        flags.computeIfAbsent(key, SET_CREATOR).add(flag);
    }

    @Override
    public boolean removeFlag(Object key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.remove(flag);
    }
}
