package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanPR implements Persister<RAProcessPlan>, Restorer<RAProcessPlan> {

    public static final RAProcessPlanPR INSTANCE = new RAProcessPlanPR();

    @Override
    public Class<RAProcessPlan> getType() {
        return RAProcessPlan.class;
    }

    @Override
    public Persistable persist(RAProcessPlan object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
        data.putLong(manager.ensureGetUID(object.getAgent()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getModel()));
        data.putInt(object.getCurrentStage().getID());
        return data;
    }

    @Override
    public RAProcessPlan initalize(Persistable persistable, RestoreManager manager) {
        return new RAProcessPlan();
    }

    @Override
    public void setup(Persistable persistable, RAProcessPlan object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEnvironment(manager.ensureGetSameClass(BasicJadexSimulationEnvironment.class));
        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
        object.setAgent(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setModel(manager.ensureGet(data.getLong()));
        object.setCurrentStage(RAStage.get(data.getInt()));
    }

    @Override
    public void finalize(Persistable persistable, RAProcessPlan object, RestoreManager manager) {
    }
}
