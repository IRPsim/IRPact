package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAModelDataPR extends BinaryPRBase<RAModelData> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAModelDataPR.class);

    public static final RAModelDataPR INSTANCE = new RAModelDataPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAModelData> getType() {
        return RAModelData.class;
    }

    @Override
    public Persistable initalizePersist(RAModelData object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.a());
        data.putDouble(object.b());
        data.putDouble(object.c());
        data.putDouble(object.d());
        data.putInt(object.getAdopterPoints());
        data.putInt(object.getInterestedPoints());
        data.putInt(object.getAwarePoints());
        data.putInt(object.getUnknownPoints());
        storeHash(object, data);
        return data;
    }

    @Override
    public RAModelData initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new RAModelData();
    }

    @Override
    public void setupRestore(Persistable persistable, RAModelData object, RestoreManager manager) {
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
}
