package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@Todo("persist aktualisieren bzw alles reinhauen was fehlt")
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
    protected BinaryJsonData doInitalizePersist(RAProcessModel object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getModelData());
        manager.prepare(object.getRnd());
        manager.prepare(object.getUncertaintySupplier());
        manager.prepare(object.getNodeFilterScheme());

        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessModel object, BinaryJsonData data, PersistManager manager) throws PersistException {
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
    protected void doSetupRestore(BinaryJsonData data, RAProcessModel object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setUncertaintySupplier(manager.ensureGet(data.getLong()));
        object.setNodeFilterScheme(manager.ensureGet(data.getLong()));
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, RAProcessModel object, RestoreManager manager) throws Exception {
        setInitialData(object);
    }

    private void setInitialData(RAProcessModel restoredInstance) throws Exception {
        InRAProcessModel inModel = (InRAProcessModel) restoreHelper.getInRoot()
                .getProcessModel(restoredInstance.getName());

        if(inModel == null) {
            throw new RestoreException("InRAProcessModel '{}' not found", restoredInstance.getName());
        }

        if(inModel.hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , inModel.getPvFile().getName());
            NPVData data = inModel.getNPVData(restoreHelper.getParser());
            restoredInstance.setNpvData(data);
        }
    }

    @Override
    protected void onChecksumMismatch(BinaryJsonData data, RAProcessModel object, RestoreManager manager) {
        object.deepHashCode();
    }
}
