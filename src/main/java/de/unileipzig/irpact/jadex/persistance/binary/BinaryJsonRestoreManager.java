package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.persistence.Restorer;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class BinaryJsonRestoreManager implements RestoreManager {

    protected final Map<BinaryJsonData, Object> restoredMap = new HashMap<>();
    protected final Map<Long, BinaryJsonData> uidData = new HashMap<>();
    protected final Map<String, Restorer<?>> restorerMap = new HashMap<>();

    protected boolean isNotRestored(BinaryJsonData data) {
        return !restoredMap.containsKey(data);
    }

    protected boolean isRestored(BinaryJsonData data) {
        return restoredMap.containsKey(data);
    }

    protected BinaryJsonData ensureGetData(long uid) {
        BinaryJsonData data = uidData.get(uid);
        if(data == null) {
            throw new NoSuchElementException("missing data for uid: " + uid);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    protected <T> T ensureGetObject(BinaryJsonData data) {
        T obj = (T) restoredMap.get(data);
        if(obj == null) {
            throw new NoSuchElementException("missing object");
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    protected <T> Restorer<T> ensureGetRestorer(String type) {
        Restorer<T> restorer = (Restorer<T>) restorerMap.get(type);
        if(restorer == null) {
            throw new NoSuchElementException("missing restorer for '" + type + "'");
        }
        return restorer;
    }

    public <T> T restore(Persistable persistable) {
        BinaryJsonData data = check(persistable);
        if(isNotRestored(data)) {
            String type = data.ensureGetType();
            Restorer<T> restorer = ensureGetRestorer(type);
            T object = restorer.initalize(data);
            restorer.setup(data, object, this);
            restoredMap.put(data, object);
            uidData.put(data.getUID(), data);
        }
        return ensureGetObject(data);
    }

    public void restore(Collection<? extends Persistable> coll) {
        //phase 1
        for(Persistable persistable: coll) {
            initalize(persistable);
        }
        //phase 2
        for(Persistable persistable: coll) {
            setup(persistable);
        }
        //phase 3
        for(Persistable persistable: coll) {
            finalize(persistable);
        }
    }

    public void initalize(Persistable persistable) {
        BinaryJsonData data = check(persistable);
        if(isRestored(data)) {
            return;
        }
        String type = data.ensureGetType();
        Restorer<?> restorer = ensureGetRestorer(type);
        Object object = restorer.initalize(data);
        restoredMap.put(data, object);
        uidData.put(data.getUID(), data);
    }

    public <T> void setup(Persistable persistable) {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        Restorer<T> restorer = ensureGetRestorer(type);
        T object = ensureGetObject(data);
        restorer.setup(data, object, this);
    }

    public <T> void finalize(Persistable persistable) {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        Restorer<T> restorer = ensureGetRestorer(type);
        T object = ensureGetObject(data);
        restorer.finalize(data, object, this);
    }

    @Override
    public <T> T ensureGet(long uid) throws NoSuchElementException {
        BinaryJsonData data = ensureGetData(uid);
        return ensureGetObject(data);
    }

    @Override
    public <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws NoSuchElementException {
        T[] out = arrCreator.apply(uids.length);
        for(int i = 0; i < uids.length; i++) {
            out[i] = ensureGet(uids[i]);
        }
        return out;
    }

    @Override
    public <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws NoSuchElementException {
        Map<K, V> out = new LinkedHashMap<>();
        for(Map.Entry<Long, Long> entry: idMap.entrySet()) {
            K key = ensureGet(entry.getKey());
            V value = ensureGet(entry.getValue());
            if(out.containsKey(key)) {
                throw new IllegalArgumentException("key '" + key + "' already exists");
            }
            out.put(key, value);
        }
        return out;
    }

    @Override
    public <T> Stream<T> streamSameClass(Class<T> c) {
        return restoredMap.values().stream()
                .filter(obj -> obj.getClass() == c)
                .map(c::cast);
    }

    @Override
    public <T> Stream<T> streamIsInstance(Class<T> c) {
        return restoredMap.values().stream()
                .filter(c::isInstance)
                .map(c::cast);
    }

    @Override
    public <T> T ensureGetSameClass(Class<T> c) throws NoSuchElementException {
        for(Object value: restoredMap.values()) {
            if(value.getClass() == c) {
                return c.cast(value);
            }
        }
        throw new NoSuchElementException(c.getName());
    }

    //=========================
    //util
    //=========================

    public static BinaryJsonData check(Persistable persistable) {
        if(persistable instanceof BinaryJsonData) {
            return (BinaryJsonData) persistable;
        } else {
            throw new IllegalArgumentException("no BinaryJsonData");
        }
    }
}
