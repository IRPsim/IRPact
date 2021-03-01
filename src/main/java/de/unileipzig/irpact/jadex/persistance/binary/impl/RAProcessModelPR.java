package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Map;

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

    @Override
    public Persistable initalizePersist(RAProcessModel object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessModel object, Persistable persistable, PersistManager manager) {
        BinaryJsonData data = check(persistable);
        data.putLong(manager.ensureGetUID(object.getModelData()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getUncertaintySupplier()));
        storeHash(object, data);

        object.deepHashCode();
    }

    @Override
    public RAProcessModel initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        RAProcessModel object = new RAProcessModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, RAProcessModel object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setUncertaintySupplier(manager.ensureGet(data.getLong()));

        setInitialData(object, manager);
    }

    private void setInitialData(RAProcessModel restoredInstance, RestoreManager manager) {
        SimulationEnvironment initialEnv = manager.getInitialInstance();
        RAProcessModel initialRA = (RAProcessModel) initialEnv.getProcessModels().getProcessModel(restoredInstance.getName());

        restoredInstance.setSlopeSupplier(initialRA.getSlopeSupplier());
        restoredInstance.setOrientationSupplier(initialRA.getOrientationSupplier());

        restoredInstance.setUnderConstructionSupplier(initialRA.getUnderConstructionSupplier());
        restoredInstance.setUnderRenovationSupplier(initialRA.getUnderRenovationSupplier());

        //restoredInstance.setUncertaintySupplier(initialRA.getUncertaintySupplier());

        restoredInstance.setNpvData(initialRA.getNpvData());
    }

    @Override
    protected void doValidationRestore(Persistable persistable, RAProcessModel object, RestoreManager manager) throws RestoreException {
        object.deepHashCode();
    }
}
