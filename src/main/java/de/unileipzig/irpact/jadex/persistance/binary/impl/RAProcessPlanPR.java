package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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
    protected BinaryJsonData doInitalizePersist(RAProcessPlan object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getCurrentStage().getID());

        manager.prepare(object.getNeed());
        manager.prepare(object.getProduct());
        manager.prepare(object.getAgent());
        manager.prepare(object.getRnd());
        manager.prepare(object.getModel());

        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessPlan object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
        data.putLong(manager.ensureGetUID(object.getAgent()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getModel()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected RAProcessPlan doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RAProcessPlan object = new RAProcessPlan();
        object.setCurrentStage(RAStage.get(data.getInt()));
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, RAProcessPlan object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
        object.setAgent(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setModel(manager.ensureGet(data.getLong()));
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, RAProcessPlan object, RestoreManager manager) {
        object.init();
    }
}
