package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModelPR extends BinaryPRBase<DiscreteTimeModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DiscreteTimeModelPR.class);

    public static final DiscreteTimeModelPR INSTANCE = new DiscreteTimeModelPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DiscreteTimeModel> getType() {
        return DiscreteTimeModel.class;
    }

    @Override
    public Persistable initalizePersist(DiscreteTimeModel object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());

        data.putInt(object.getStartYear());
        data.putLong(object.getStoredDelta());
        data.putLong(object.getStoredTimePerTickInMs());
        data.putDouble(object.getStartTick());
        data.putDouble(object.getEndTick());
        data.putDouble(object.getNowTick());
        data.putDouble(object.getTickModifier());
        data.putLong(object.getNowStamp().getEpochMilli());
        data.putLong(object.startTime().getEpochMilli());
        data.putLong(object.endTime().getEpochMilli());
        data.putDouble(object.getDelayTillEnd());

        storeHash(object, data);
        return data;
    }

    @Override
    public DiscreteTimeModel initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        DiscreteTimeModel object = new DiscreteTimeModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, DiscreteTimeModel object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(JadexSimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDirect(
                data.getInt(),
                data.getLong(),
                data.getLong(),
                data.getDouble(),
                data.getDouble(),
                data.getDouble(),
                data.getDouble(),
                new BasicTimestamp(data.getLong()),
                new BasicTimestamp(data.getLong()),
                new BasicTimestamp(data.getLong()),
                data.getDouble()
        );
    }
}
