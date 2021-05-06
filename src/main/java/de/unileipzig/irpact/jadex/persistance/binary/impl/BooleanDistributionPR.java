package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionPR extends BinaryPRBase<BooleanDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BooleanDistributionPR.class);

    public static final BooleanDistributionPR INSTANCE = new BooleanDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BooleanDistribution> getType() {
        return BooleanDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BooleanDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getFalseValue());
        data.putDouble(object.getTrueValue());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BooleanDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BooleanDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BooleanDistribution object = new BooleanDistribution();
        object.setName(data.getText());
        object.setFalseValue(data.getDouble());
        object.setTrueValue(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BooleanDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
