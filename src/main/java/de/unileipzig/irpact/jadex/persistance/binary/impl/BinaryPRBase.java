package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryPersister;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryRestorer;
import de.unileipzig.irpact.jadex.persistance.binary.RestoreHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings({"RedundantThrows", "unused", "UnnecessaryLocalVariable"})
public abstract class BinaryPRBase<T> implements BinaryPersister<T>, BinaryRestorer<T> {

    protected abstract IRPLogger log();

    protected static BinaryJsonData initData(Object obj, PersistManager manager) {
        return initDataWithClass(obj.getClass(), manager);
    }

    protected static BinaryJsonData initDataWithClass(Class<?> c, PersistManager manager) {
        BinaryJsonData data = BinaryJsonData.init(IRPactJson.SMILE.getNodeFactory(), manager.newUID(), c);
        //System.out.println(data.getUID() + " " + c.getName()); //TODO
        data.setPutMode();
        return data;
    }

    protected static BinaryJsonData check(Persistable persistable) {
        if(persistable instanceof BinaryJsonData) {
            return (BinaryJsonData) persistable;
        } else {
            throw new IllegalArgumentException("no BinaryJsonData");
        }
    }

    //=========================
    //persist
    //=========================

    @Override
    public final Persistable initalizePersist(T object, PersistManager manager) throws PersistException {
        try {
            return doInitalizePersist(object, manager);
        } catch (UncheckedPersistException e) {
            throw e.getCause();
        }
    }

    protected abstract BinaryJsonData doInitalizePersist(T object, PersistManager manager) throws PersistException;

    @Override
    public final void setupPersist(T object, Persistable persistable, PersistManager manager) throws PersistException {
        try {
            BinaryJsonData data = check(persistable);
            doSetupPersist(object, data, manager);
            storeHash(object, data);
        } catch (UncheckedPersistException e) {
            throw e.getCause();
        }
    }

    //ueberschreiben, falls benoetigt
    protected void doSetupPersist(T object, BinaryJsonData data, PersistManager manager) throws PersistException {
    }

    protected void storeHash(T object, BinaryJsonData data) {
        if(object instanceof ChecksumComparable) {
            data.putInt(((ChecksumComparable) object).getChecksum());
        } else {
            log().trace("type '{}' not hashable", object.getClass().getName());
        }
    }

    //=========================
    //restore
    //=========================

    protected RestoreHelper restoreHelper;

    @Override
    public void setRestoreHelper(RestoreHelper restoreHelper) {
        this.restoreHelper = restoreHelper;
    }

    protected RestoreHelper getRestoreHelper() {
        return restoreHelper;
    }

    @Override
    public final T initalizeRestore(Persistable persistable, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = check(persistable);
        T object = doInitalizeRestore(data, manager);
        return object;
    }

    protected abstract T doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException;

    @Override
    public final void setupRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = check(persistable);
        doSetupRestore(data, object, manager);
    }

    //ueberschreiben, falls benoetigt
    protected void doSetupRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
    }

    @Override
    public final void finalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = check(persistable);
        doFinalizeRestore(data, object, manager);
    }

    //ueberschreiben, falls benoetigt
    protected void doFinalizeRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
    }

    @Override
    public final void validateRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = check(persistable);
        doValidationRestore(data, object, manager);
        checkHash(data, object, manager);
    }

    //ueberschreiben, falls benoetigt
    protected void doValidationRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
    }

    protected void checkHash(BinaryJsonData data, T object, RestoreManager manager) {
        if(object instanceof ChecksumComparable) {
            int storedHash = data.getInt();
            int restoredHash = ((ChecksumComparable) object).getChecksum();
            if(!checkHash(object, storedHash, restoredHash)) {
                onChecksumMismatch(data, object, manager);
            }
        } else {
            log().trace("type '{}' not hashable", object.getClass().getName());
        }
    }

    protected boolean checkHash(T object, int storedHash, int restoredHash) {
        if(storedHash != restoredHash) {
            log().warn(
                    "[{}] hash mismatch: stored={} != restored={}",
                    object.getClass().getSimpleName(),
                    Integer.toHexString(storedHash),
                    Integer.toHexString(restoredHash)
            );
            return false;
        } else {
            return true;
        }
    }

    protected void onChecksumMismatch(BinaryJsonData data, T object, RestoreManager manager) {
    }
}
