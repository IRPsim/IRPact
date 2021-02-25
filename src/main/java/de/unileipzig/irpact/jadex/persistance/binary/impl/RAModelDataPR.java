package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class RAModelDataPR implements Persister<RAModelData>, Restorer<RAModelData> {

    public static final RAModelDataPR INSTANCE = new RAModelDataPR();

    @Override
    public Class<RAModelData> getType() {
        return RAModelData.class;
    }

    @Override
    public Persistable persist(RAModelData object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.a());
        data.putDouble(object.b());
        data.putDouble(object.c());
        data.putDouble(object.d());
        data.putInt(object.getAdopterPoints());
        data.putInt(object.getInterestedPoints());
        data.putInt(object.getAwarePoints());
        data.putInt(object.getUnknownPoints());
        return data;
    }

    @Override
    public RAModelData initalize(Persistable persistable) {
        return new RAModelData();
    }

    @Override
    public void setup(Persistable persistable, RAModelData object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setA(data.getDouble());
        object.setB(data.getDouble());
        object.setC(data.getDouble());
        object.setD(data.getDouble());
        object.setAdopterPoints(data.getInt());
        object.setInterestedPoints(data.getInt());
        object.setAwarePoints(data.getInt());
        object.setUnknownPoints(data.getInt());
    }

    @Override
    public void finalize(Persistable persistable, RAModelData object, RestoreManager manager) {
    }
}
