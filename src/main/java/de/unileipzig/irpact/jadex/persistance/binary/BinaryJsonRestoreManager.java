package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

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
    protected final RestoreHelper restoreHelper = new RestoreHelper();
    protected final List<Persistable> persistables = new ArrayList<>();
    protected boolean hasValidationChecksum;
    protected int validationChecksum;
    protected Object restoredInstance;

    public BinaryJsonRestoreManager() {
        init();
    }

    private void init() {
        BinaryJsonUtil.registerDefaults(this);
    }

    public void setCommandLineOptions(MainCommandLineOptions options) {
        restoreHelper.setOptions(options);
    }

    public void setInRoot(InRoot root) {
        restoreHelper.setInRoot(root);
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

    protected BinaryJsonData ensureGetData(long uid) throws RestoreException {
        BinaryJsonData data = uidData.get(uid);
        if(data == null) {
            throw new RestoreException("missing data for uid: " + uid);
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
    public void unregisterAll() {
        persistables.clear();
        restoredMap.clear();
        uidData.clear();
        restoredMap.clear();
        restoreHelper.clear();
        clearRestoredInstance();
        clearValidationChecksum();
    }

    @Override
    public void register(Persistable persistable) {
        persistables.add(persistable);
    }

    @Override
    public void register(Collection<? extends Persistable> coll) {
        persistables.addAll(coll);
    }

    @Override
    public void restore() throws RestoreException {
        restore(persistables);
    }

    protected void restore(Collection<? extends Persistable> coll) throws RestoreException {
        if(coll == null) {
            throw new RestoreException("persistables is null");
        }
        if(coll.isEmpty()) {
            throw new RestoreException("persistables is empty");
        }

        clearRestoredInstance();
        clearValidationChecksum();

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

    protected void clearRestoredInstance() {
        setRestoredInstance(null);
    }

    @Override
    public void setRestoredInstance(Object restored) {
        this.restoredInstance = restored;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRestoredInstance() throws NoSuchElementException {
        if(restoredInstance == null) {
            throw new NoSuchElementException();
        }
        return (T) restoredInstance;
    }

    private void clearValidationChecksum() {
        validationChecksum = 0;
        hasValidationChecksum = false;
    }

    @Override
    public void setValidationChecksum(int checksum) {
        this.validationChecksum = checksum;
        hasValidationChecksum = true;
    }

    @Override
    public int getValidationChecksum() {
        if(!hasValidationChecksum) {
            throw new NoSuchElementException();
        }
        return validationChecksum;
    }

    @Override
    public <T> T ensureGet(long uid) throws RestoreException {
        BinaryJsonData data = ensureGetData(uid);
        return ensureGetObject(data);
    }

    @Override
    public <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws RestoreException {
        T[] out = arrCreator.apply(uids.length);
        for(int i = 0; i < uids.length; i++) {
            out[i] = ensureGet(uids[i]);
        }
        return out;
    }

    @Override
    public <T> boolean ensureGetAll(long[] uids, Collection<? super T> target) throws RestoreException {
        boolean changed = false;
        for(long uid: uids) {
            T element = ensureGet(uid);
            changed |= target.add(element);
        }
        return changed;
    }

    @Override
    public <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws RestoreException {
        Map<K, V> out = new LinkedHashMap<>();
        for(Map.Entry<Long, Long> entry: idMap.entrySet()) {
            K key = ensureGet(entry.getKey());
            V value = ensureGet(entry.getValue());
            if(out.containsKey(key)) {
                throw new RestoreException("key '" + key + "' already exists");
            }
            out.put(key, value);
        }
        return out;
    }

    @Override
    public <T> T ensureGetSameClass(Class<T> c) throws RestoreException {
        for(Object value: restoredMap.values()) {
            if(value.getClass() == c) {
                return c.cast(value);
            }
        }
        throw new RestoreException(c.getName());
    }

    @Override
    public <T> T ensureGetInstanceOf(Class<T> c) throws RestoreException {
        for(Object value: restoredMap.values()) {
            if(c.isInstance(value)) {
                return c.cast(value);
            }
        }
        throw new RestoreException(c.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T ensureGetByName(String name) throws RestoreException {
        for(Object value: restoredMap.values()) {
            if(value instanceof Nameable) {
                Nameable n = (Nameable) value;
                if(Objects.equals(name, n.getName())) {
                    return (T) n;
                }
            }
        }
        throw new RestoreException(name);
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
