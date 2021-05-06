package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.start.CommandLineOptions;

import java.util.*;
import java.util.function.IntFunction;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class BinaryJsonRestoreManager implements RestoreManager {

    public static final String INITIAL_INSTANCE = "_INITIAL_INSTANCE_";
    public static final String RESTORED_INSTANCE = "_RESTORED_INSTANCE_";
    public static final String VALIDATION_CHECKSUM = "_VALIDATION_CHECKSUM_";
    public static final String IN_ROOT = "_IN_ROOT_";
    public static final String PARAM = "_PARAM_";

    protected final Map<BinaryJsonData, Object> restoredMap = new LinkedHashMap<>();
    protected final Map<Long, BinaryJsonData> uidData = new LinkedHashMap<>();
    protected final Map<String, BinaryRestorer<?>> restorerMap = new LinkedHashMap<>();
    protected final Map<Object, Object> cache = new LinkedHashMap<>();
    protected final RestoreHelper restoreHelper = new RestoreHelper();

    public BinaryJsonRestoreManager() {
        init();
    }

    private void init() {
        BinaryJsonUtil.registerDefaults(this);
    }

    public void setCommandLineOptions(CommandLineOptions options) {
        restoreHelper.setOptions(options);
    }

    public <T> boolean register(BinaryRestorer<T> restorer) {
        if(restorerMap.containsKey(restorer.getType().getName())) {
            return false;
        } else {
            restorerMap.put(restorer.getType().getName(), restorer);
            return true;
        }
    }

    public <T> void ensureRegister(BinaryRestorer<T> restorer) {
        if(!register(restorer)) {
            throw new IllegalArgumentException("class '" + restorer.getType().getName() + "' already exists");
        }
    }

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
    protected <T> BinaryRestorer<T> ensureGetRestorer(String type) {
        BinaryRestorer<T> restorer = (BinaryRestorer<T>) restorerMap.get(type);
        if(restorer == null) {
            throw new NoSuchElementException("missing restorer for '" + type + "'");
        }
        return restorer;
    }

    @Override
    public void restore(Collection<? extends Persistable> coll) throws RestoreException {
        setRestoredInstance(null);

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
        //phase 4
        for(Persistable persistable: coll) {
            validation(persistable);
        }
    }

    protected void initalize(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        if(isRestored(data)) {
            return;
        }
        String type = data.ensureGetType();
        BinaryRestorer<?> restorer = ensureGetRestorer(type);
        restorer.setRestoreHelper(restoreHelper);
        Object object = restorer.initalizeRestore(data, this);
        restoredMap.put(data, object);
        uidData.put(data.getUID(), data);
    }

    protected <T> void setup(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        BinaryRestorer<T> restorer = ensureGetRestorer(type);
        restorer.setRestoreHelper(restoreHelper);
        T object = ensureGetObject(data);
        restorer.setupRestore(data, object, this);
    }

    protected <T> void finalize(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        BinaryRestorer<T> restorer = ensureGetRestorer(type);
        restorer.setRestoreHelper(restoreHelper);
        T object = ensureGetObject(data);
        restorer.finalizeRestore(data, object, this);
    }

    protected <T> void validation(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        BinaryRestorer<T> restorer = ensureGetRestorer(type);
        restorer.setRestoreHelper(restoreHelper);
        T object = ensureGetObject(data);
        restorer.validateRestore(data, object, this);
    }

    protected void checkedSet(Object key, Object value, String msg) {
        if(value == null) {
            cache.remove(key);
        } else {
            if(cache.containsKey(key)) {
                throw new IllegalStateException(msg);
            }
            cache.put(key, value);
        }
    }

    protected Object checkedGet(Object key, String msg) {
        if(!cache.containsKey(key)) {
            throw new NoSuchElementException(msg);
        }
        return cache.get(key);
    }

    @Override
    public void setInitialInstance(Object initial) {
        checkedSet(INITIAL_INSTANCE, initial, "initial instance already set");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInitialInstance() {
        return (T) checkedGet(INITIAL_INSTANCE, "initial instance");
    }

    @Override
    public void setRestoredInstance(Object restored) {
        checkedSet(RESTORED_INSTANCE, restored, "restored instance already set");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRestoredInstance() {
        return (T) checkedGet(RESTORED_INSTANCE, "restored instance");
    }

    @Override
    public void setValidationChecksum(int checksum) {
        checkedSet(VALIDATION_CHECKSUM, checksum, "validation checksum already set");
    }

    @Override
    public int getValidationChecksum() {
        return (Integer) checkedGet(VALIDATION_CHECKSUM, "validation checksum");
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
    public <T> boolean ensureGetAll(long[] uids, Collection<? super T> target) throws NoSuchElementException {
        boolean changed = false;
        for(long uid: uids) {
            T element = ensureGet(uid);
            changed |= target.add(element);
        }
        return changed;
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
    public <T> T ensureGetSameClass(Class<T> c) throws NoSuchElementException {
        for(Object value: restoredMap.values()) {
            if(value.getClass() == c) {
                return c.cast(value);
            }
        }
        throw new NoSuchElementException(c.getName());
    }

    @Override
    public <T> T ensureGetInstanceOf(Class<T> c) throws NoSuchElementException {
        for(Object value: restoredMap.values()) {
            if(c.isInstance(value)) {
                return c.cast(value);
            }
        }
        throw new NoSuchElementException(c.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T ensureGetByName(String name) throws NoSuchElementException {
        for(Object value: restoredMap.values()) {
            if(value instanceof Nameable) {
                Nameable n = (Nameable) value;
                if(Objects.equals(name, n.getName())) {
                    return (T) n;
                }
            }
        }
        throw new NoSuchElementException(name);
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
