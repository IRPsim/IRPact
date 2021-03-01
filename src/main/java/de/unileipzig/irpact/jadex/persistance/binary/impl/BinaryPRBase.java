package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class BinaryPRBase<T> implements Persister<T>, Restorer<T> {

    protected abstract IRPLogger log();

    //=========================
    //persist
    //=========================

    protected BinaryJsonData check(Persistable persistable) {
        if(persistable instanceof BinaryJsonData) {
            return (BinaryJsonData) persistable;
        } else {
            throw new IllegalArgumentException("no BinaryJsonData");
        }
    }

//    protected BinaryJsonData doPersist(T object, PersistManager manager) {
//
//        return null;
//    }

    @Override
    public final void setupPersist(T object, Persistable persistable, PersistManager manager) {
        doSetupPersist(object, persistable, manager);
    }

    protected void doSetupPersist(T object, Persistable persistable, PersistManager manager) {
    }

    protected void storeHash(T object, BinaryJsonData data) {
        if(object instanceof IsEquals) {
            data.putInt(((IsEquals) object).getHashCode());
        } else {
            log().debug("type '{}' not hashable", object.getClass().getName());
        }
    }

    //=========================
    //restore
    //=========================

    @Override
    public void finalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        doFinalizeRestore(persistable, object, manager);
    }

    protected void doFinalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
    }

    @Override
    public final void validateRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        doValidationRestore(persistable, object, manager);
        checkHash(persistable, object, manager);
    }

    protected void doValidationRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
    }

    protected void checkHash(Persistable persistable, T object, RestoreManager manager) {
        if(object instanceof IsEquals) {
            BinaryJsonData data = check(persistable);
            int storedHash = data.getInt();
            int restoredHash = ((IsEquals) object).getHashCode();
            if(storedHash != restoredHash) {
                log().warn(
                        "[{}] hash mismatch: stored={} != restored={}",
                        object.getClass().getSimpleName(),
                        Integer.toHexString(storedHash),
                        Integer.toHexString(restoredHash)
                );
                onHashMismatch(persistable, object, manager);
            }
        } else {
            log().debug("type '{}' not hashable", object.getClass().getName());
        }
    }

    protected void onHashMismatch(Persistable persistable, T object, RestoreManager manager) {
    }
}
