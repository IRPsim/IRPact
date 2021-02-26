package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
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
        data.putLong(manager.ensureGetUID(object.getModelData()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        return data;
    }

    @Override
    public RAProcessModel initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        RAProcessModel object = new RAProcessModel();
        object.setName(data.getText());
        return object;
    }

    private void setInitialData(RAProcessModel restoredInstance, RestoreManager manager) {
        SimulationEnvironment initialEnv = manager.getInitialInstance();
        RAProcessModel initialRA = (RAProcessModel) initialEnv.getProcessModels().getProcessModel(restoredInstance.getName());

        restoredInstance.setSlopeSupplier(initialRA.getSlopeSupplier());
        restoredInstance.setOrientationSupplier(initialRA.getOrientationSupplier());

        restoredInstance.setUnderConstructionSupplier(initialRA.getUnderConstructionSupplier());
        restoredInstance.setUnderRenovationSupplier(initialRA.getUnderRenovationSupplier());

        restoredInstance.setUncertaintySupplier(initialRA.getUncertaintySupplier());
    }

    @Override
    public void setup(Persistable persistable, RAProcessModel object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));

        setInitialData(object, manager);
    }

    @Override
    public void finalize(Persistable persistable, RAProcessModel object, RestoreManager manager) {
    }
}
