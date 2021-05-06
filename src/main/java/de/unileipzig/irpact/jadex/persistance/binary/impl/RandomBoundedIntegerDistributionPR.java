package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.RandomBoundedIntegerDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistributionPR extends BinaryPRBase<RandomBoundedIntegerDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RandomBoundedIntegerDistributionPR.class);

    public static final RandomBoundedIntegerDistributionPR INSTANCE = new RandomBoundedIntegerDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RandomBoundedIntegerDistribution> getType() {
        return RandomBoundedIntegerDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RandomBoundedIntegerDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(RandomBoundedIntegerDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RandomBoundedIntegerDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RandomBoundedIntegerDistribution object = new RandomBoundedIntegerDistribution();
        object.setName(data.getText());
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, RandomBoundedIntegerDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
