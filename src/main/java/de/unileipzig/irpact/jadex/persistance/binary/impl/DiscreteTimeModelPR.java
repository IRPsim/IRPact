package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModelPR implements Persister<DiscreteTimeModel>, Restorer<DiscreteTimeModel> {

    public static final DiscreteTimeModelPR INSTANCE = new DiscreteTimeModelPR();

    @Override
    public Class<DiscreteTimeModel> getType() {
        return DiscreteTimeModel.class;
    }

    @Override
    public Persistable persist(DiscreteTimeModel object, PersistManager manager) {
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

        return data;
    }

    @Override
    public DiscreteTimeModel initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        DiscreteTimeModel object = new DiscreteTimeModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, DiscreteTimeModel object, RestoreManager manager) {
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

    @Override
    public void finalize(Persistable persistable, DiscreteTimeModel object, RestoreManager manager) {
    }
}
