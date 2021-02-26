package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.ra.RADataSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class RADataSupplierPR implements Persister<RADataSupplier>, Restorer<RADataSupplier> {

    public static final RADataSupplierPR INSTANCE = new RADataSupplierPR();

    @Override
    public Class<RADataSupplier> getType() {
        return RADataSupplier.class;
    }

    @Override
    public Persistable persist(RADataSupplier object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getDistribution()));
        return data;
    }

    @Override
    public RADataSupplier initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        RADataSupplier object = new RADataSupplier();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, RADataSupplier object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, RADataSupplier object, RestoreManager manager) {
    }
}
