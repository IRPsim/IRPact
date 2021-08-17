package de.unileipzig.irpact.core.persistence.binaryjson;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings({"RedundantThrows", "unused", "UnnecessaryLocalVariable"})
public abstract class BinaryPRBase<T> implements BinaryPersister<T>, BinaryRestorer<T> {

    protected abstract IRPLogger log();

    protected BinaryJsonData initData(Object obj, PersistManager manager) {
        return initDataWithClass(obj.getClass(), manager);
    }

    protected BinaryJsonData initDataWithClass(Class<?> c, PersistManager manager) {
        BinaryJsonData data = BinaryJsonData.init(JsonUtil.SMILE.getNodeFactory(), manager.newUID(), c, getRestoreHelper().getClassManager());
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
            storeChecksum(object, data);

            if(restoreHelper.isPrintLoggableOnPersist() && object instanceof LoggableChecksum) {
                ((LoggableChecksum) object).logChecksums();
            }
        } catch (UncheckedPersistException e) {
            throw e.getCause();
        }
    }

    //ueberschreiben, falls benoetigt
    protected void doSetupPersist(T object, BinaryJsonData data, PersistManager manager) throws PersistException {
    }

    protected void storeChecksum(T object, BinaryJsonData data) {
        if(object instanceof ChecksumComparable) {
            data.putInt(((ChecksumComparable) object).getChecksum());
        } else {
            log().trace("type '{}' has no checksum", object.getClass().getName());
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

    protected JadexSimulationEnvironment getEnvironment(RestoreManager manager) throws RestoreException {
        return manager.ensureGetInstanceOf(JadexSimulationEnvironment.class);
    }

    public AgentManager getAgentManager(RestoreManager manager) throws RestoreException {
        return getEnvironment(manager).getAgents();
    }

    @Override
    public final T initalizeRestore(Persistable persistable, RestoreManager manager) throws RestoreException {
        try {
            BinaryJsonData data = check(persistable);
            T object = doInitalizeRestore(data, manager);
            return object;
        } catch (UncheckedRestoreException e) {
            throw e.getCause();
        }
    }

    protected abstract T doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException;

    @Override
    public final void setupRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        try {
            BinaryJsonData data = check(persistable);
            doSetupRestore(data, object, manager);
        } catch (UncheckedRestoreException e) {
            throw e.getCause();
        }
    }

    //ueberschreiben, falls benoetigt
    protected void doSetupRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
    }

    @Override
    public final void finalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        try {
            BinaryJsonData data = check(persistable);
            doFinalizeRestore(data, object, manager);
        } catch (UncheckedRestoreException e) {
            throw e.getCause();
        } catch (RestoreException e) {
            throw e;
        } catch (Exception e) {
            throw new RestoreException(e);
        }
    }

    //ueberschreiben, falls benoetigt
    protected void doFinalizeRestore(BinaryJsonData data, T object, RestoreManager manager) throws Exception {
    }

    @Override
    public final void validateRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException {
        try {
            BinaryJsonData data = check(persistable);
            doValidationRestore(data, object, manager);
            checkChecksum(data, object, manager);
        } catch (UncheckedRestoreException e) {
            throw e.getCause();
        }
    }

    //ueberschreiben, falls benoetigt
    protected void doValidationRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
    }

    protected void checkChecksum(BinaryJsonData data, T object, RestoreManager manager) {
        if(object instanceof ChecksumComparable) {
            int storedHash = data.getInt();
            int restoredChecksum = ((ChecksumComparable) object).getChecksum();
            if(!checkChecksum(object, storedHash, restoredChecksum)) {
                onChecksumMismatch(data, object, manager);
            }
        } else {
            log().trace("type '{}' has no checksum", object.getClass().getName());
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean checkChecksum(T object, int storedChecksum, int restoredChecksum) {
        if(storedChecksum != restoredChecksum) {
            log().warn(
                    "[{}] checksum mismatch: stored={} != restored={}",
                    object.getClass().getSimpleName(),
                    Integer.toHexString(storedChecksum),
                    Integer.toHexString(restoredChecksum)
            );
            return false;
        } else {
            return true;
        }
    }

    protected void onChecksumMismatch(BinaryJsonData data, T object, RestoreManager manager) {
        if(object instanceof LoggableChecksum) {
            ((LoggableChecksum) object).logChecksums();
        }
    }
}
