package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class MapDataStore extends NameableBase implements DataStore {

    private static final Function<String, Set<Object>> SET_CREATOR = key -> new LinkedHashSet<>();

    protected MapSupplier supplier;
    protected Map<String, Object> data;
    protected Map<String, Set<Object>> flags;

    public MapDataStore() {
        this(MapSupplier.LINKED);
    }

    public MapDataStore(MapSupplier supplier) {
        this(supplier, supplier.newMap());
    }

    public MapDataStore(MapSupplier supplier, Map<String, Object> data) {
        this.supplier = supplier;
        this.data = data;
        this.flags = supplier.newMap();
    }

    protected MapDataStore(MapDataStore other) {
        this.supplier = other.supplier;
        this.data = supplier.newMap();
        this.flags = supplier.newMap();

        data.putAll(other.data);
        for(Map.Entry<String, Set<Object>> entry: other.flags.entrySet()) {
            Set<Object> set = SET_CREATOR.apply(entry.getKey());
            set.addAll(entry.getValue());
            flags.put(entry.getKey(), set);
        }
    }

    public MapDataStore copy() {
        return new MapDataStore(this);
    }

    protected static void validateKey(String key) {
        if(key == null) throw new NullPointerException("key is null");
    }

    @Override
    public void removeAll() {
        data.clear();
    }

    @Override
    public boolean contains(String key) {
        return key != null && data.containsKey(key);
    }

    @Override
    public void put(String key, Object obj) {
        validateKey(key);
        data.put(key, obj);
    }

    @Override
    public boolean remove(String key) {
        return key != null && data.remove(key) != null;
    }

    @Override
    public Object get(String key) {
        validateKey(key);
        return data.get(key);
    }

    @Override
    public boolean hasFlag(String key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.contains(flag);
    }

    @Override
    public void setFlag(String key, Object flag) {
        validateKey(key);
        flags.computeIfAbsent(key, SET_CREATOR).add(flag);
    }

    @Override
    public boolean removeFlag(String key, Object flag) {
        if(key == null) return false;
        Set<Object> set = flags.get(key);
        return set != null && set.remove(flag);
    }
}
