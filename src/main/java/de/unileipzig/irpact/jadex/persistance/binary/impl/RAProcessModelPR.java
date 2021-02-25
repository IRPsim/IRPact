package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class RAProcessModelPR implements Persister<RAProcessModel>, Restorer<RAProcessModel> {

    public static final RAProcessModelPR INSTANCE = new RAProcessModelPR();

    @Override
    public Class<RAProcessModel> getType() {
        return RAProcessModel.class;
    }

    @Override
    public Persistable persist(RAProcessModel object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        if(object.getOrientationSupplier() == null) {
            data.putNothing();
        } else {
            data.putLong(manager.ensureGetUID(object.getOrientationSupplier()));
        }
        if(object.getSlopeSupplier() == null) {
            data.putNothing();
        } else {
            data.putLong(manager.ensureGetUID(object.getSlopeSupplier()));
        }
        data.putLong(manager.ensureGetUID(object.getModelData()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        return data;
    }

    @Override
    public RAProcessModel initalize(Persistable persistable) {
        return new RAProcessModel();
    }

    @Override
    public void setup(Persistable persistable, RAProcessModel object, RestoreManager manager) {
        if(true) throw new TodoException();
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        long oriSuppId = data.getLong();
        if(oriSuppId != BinaryJsonData.NOTHING_ID) {
//            object.setOrientationSupplier(manager.ensureGet(oriSuppId));
        }
        long sloSuppId = data.getLong();
        if(sloSuppId != BinaryJsonData.NOTHING_ID) {
//            object.setSlopeSupplier(manager.ensureGet(sloSuppId));
        }
        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, RAProcessModel object, RestoreManager manager) {
    }
}
