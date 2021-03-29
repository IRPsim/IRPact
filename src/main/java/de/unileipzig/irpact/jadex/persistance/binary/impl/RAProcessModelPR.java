package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessModelPR extends BinaryPRBase<RAProcessModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModelPR.class);

    public static final RAProcessModelPR INSTANCE = new RAProcessModelPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAProcessModel> getType() {
        return RAProcessModel.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RAProcessModel object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getModelData());
        manager.prepare(object.getRnd());
        manager.prepare(object.getUncertaintySupplier());
        manager.prepare(object.getNodeFilterScheme());

        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessModel object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getModelData()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getUncertaintySupplier()));
        data.putLong(manager.ensureGetUID(object.getNodeFilterScheme()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RAProcessModel doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        RAProcessModel object = new RAProcessModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, RAProcessModel object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setUncertaintySupplier(manager.ensureGet(data.getLong()));
        object.setNodeFilterScheme(manager.ensureGet(data.getLong()));
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, RAProcessModel object, RestoreManager manager) {
        setInitialData(object, manager);
    }

    private void setInitialData(RAProcessModel restoredInstance, RestoreManager manager) {
        SimulationEnvironment initialEnv = manager.getInitialInstance();
        RAProcessModel initialRA = (RAProcessModel) initialEnv.getProcessModels().getProcessModel(restoredInstance.getName());

        restoredInstance.setUnderConstructionSupplier(initialRA.getUnderConstructionSupplier());
        restoredInstance.setUnderRenovationSupplier(initialRA.getUnderRenovationSupplier());

        restoredInstance.setNpvData(initialRA.getNpvData());
    }

    @Override
    protected void onHashMismatch(BinaryJsonData data, RAProcessModel object, RestoreManager manager) {
        object.deepHashCode();
    }
}
