package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DiscreteTimeModel object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

//        data.putInt(object.getStartYear());
        data.putLong(object.getStoredDelta());
        data.putLong(object.getStoredTimePerTickInMs());
//        data.putDouble(object.getStartTick());
//        data.putDouble(object.getEndTick());
//        data.putDouble(object.getNowTick());
//        data.putDouble(object.getTickModifier());
//        data.putLong(object.getNowStamp().getEpochMilli());
//        data.putLong(object.startTime().getEpochMilli());
//        data.putLong(object.endTime().getEpochMilli());
//        data.putDouble(object.getDelayTillEnd());

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DiscreteTimeModel doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        DiscreteTimeModel object = new DiscreteTimeModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DiscreteTimeModel object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(JadexSimulationEnvironment.class));

        object.setStoredDelta(data.getLong());
        object.setStoredTimePerTickInMs(data.getLong());
//        object.setDirect(
//                data.getInt(),
//                data.getLong(),
//                data.getLong(),
//                data.getDouble(),
//                data.getDouble(),
//                data.getDouble(),
//                data.getDouble(),
//                new BasicTimestamp(data.getLong()),
//                new BasicTimestamp(data.getLong()),
//                new BasicTimestamp(data.getLong()),
//                data.getDouble()
//        );
    }
}
