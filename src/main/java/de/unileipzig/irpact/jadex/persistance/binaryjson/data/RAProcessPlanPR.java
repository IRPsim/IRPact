package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanPR extends BinaryPRBase<RAProcessPlan> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlanPR.class);

    public static final RAProcessPlanPR INSTANCE = new RAProcessPlanPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAProcessPlan> getType() {
        return RAProcessPlan.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RAProcessPlan object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getStage().getID());
        data.putBoolean(object.isUnderConstruction());
        data.putBoolean(object.isUnderRenovation());

        manager.prepare(object.getNeed());
        manager.prepare(object.getProduct());
        manager.prepare(object.getAgent());
        manager.prepare(object.getRnd());
        manager.prepare(object.getModel());
        manager.prepare(object.getNetworkFilter());
        manager.prepare(object.getUncertainty());

        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessPlan object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
        data.putLong(manager.ensureGetUID(object.getAgent()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getModel()));
        data.putLong(manager.ensureGetUID(object.getNetworkFilter()));
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected RAProcessPlan doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RAProcessPlan object = new RAProcessPlan();
        object.setStage(RAStage.get(data.getInt()));
        object.setUnderConstruction(data.getBoolean());
        object.setUnderRenovation(data.getBoolean());

        return object;
    }

    protected Uncertainty restoredUncertainty;

    @Override
    protected void doSetupRestore(BinaryJsonData data, RAProcessPlan object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
        object.setAgent(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setModel(manager.ensureGet(data.getLong()));
        object.setNetworkFilter(manager.ensureGet(data.getLong()));
        restoredUncertainty = manager.ensureGet(data.getLong());
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, RAProcessPlan object, RestoreManager manager) throws RestoreException {
        if(restoredUncertainty == null) {
            throw new RestoreException("missing restored uncertainty");
        }
        object.getModel().getUncertaintyCache().registerUncertainty(object.getAgent(), restoredUncertainty, false, false);
    }
}
