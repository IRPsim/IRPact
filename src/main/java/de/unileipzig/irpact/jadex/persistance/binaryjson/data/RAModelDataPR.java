package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAModelDataPR extends BinaryPRBase<RAModelData> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAModelDataPR.class);

    public static final RAModelDataPR INSTANCE = new RAModelDataPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAModelData> getType() {
        return RAModelData.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RAModelData object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putDouble(object.a());
        data.putDouble(object.b());
        data.putDouble(object.c());
        data.putDouble(object.d());
        data.putInt(object.getAdopterPoints());
        data.putInt(object.getInterestedPoints());
        data.putInt(object.getAwarePoints());
        data.putInt(object.getUnknownPoints());
        data.putDouble(object.getLogisticFactor());
        data.putDouble(object.getWeightFT());
        data.putDouble(object.getWeightNPV());
        data.putDouble(object.getWeightSocial());
        data.putDouble(object.getWeightLocal());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RAModelData doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RAModelData object = new RAModelData();
        object.setA(data.getDouble());
        object.setB(data.getDouble());
        object.setC(data.getDouble());
        object.setD(data.getDouble());
        object.setAdopterPoints(data.getInt());
        object.setInterestedPoints(data.getInt());
        object.setAwarePoints(data.getInt());
        object.setUnknownPoints(data.getInt());
        object.setLogisticFactor(data.getDouble());
        object.setWeightFT(data.getDouble());
        object.setWeightNPV(data.getDouble());
        object.setWeightSocial(data.getDouble());
        object.setWeightLocal(data.getDouble());
        return object;
    }
}
