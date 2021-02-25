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
        data.putLong(object.getStoredDelta());
        data.putLong(object.getStoredTimePerTickInMs());
        data.putDouble(object.getTickModifier());
        data.putLong(object.getNowStamp().getEpochMilli());
        //falls noetig: ZoneId speicherbar ueber getId():String und dann of(String ZoneId)
        return data;
    }

    @Override
    public DiscreteTimeModel initalize(Persistable persistable) {
        return new DiscreteTimeModel();
    }

    @Override
    public void setup(Persistable persistable, DiscreteTimeModel object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setStoredDelta(data.getLong());
        object.setStoredTimePerTickInMs(data.getLong());
        object.setTickModifier(data.getDouble());
        object.setNowStamp(new BasicTimestamp(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, DiscreteTimeModel object, RestoreManager manager) {
    }
}
