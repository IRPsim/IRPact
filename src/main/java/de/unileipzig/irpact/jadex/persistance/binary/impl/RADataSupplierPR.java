package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RADataSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RADataSupplierPR extends BinaryPRBase<RADataSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RADataSupplierPR.class);

    public static final RADataSupplierPR INSTANCE = new RADataSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RADataSupplier> getType() {
        return RADataSupplier.class;
    }

    @Override
    public Persistable initalizePersist(RADataSupplier object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getDistribution()));
        storeHash(object, data);
        return data;
    }

    @Override
    public RADataSupplier initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        RADataSupplier object = new RADataSupplier();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, RADataSupplier object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
