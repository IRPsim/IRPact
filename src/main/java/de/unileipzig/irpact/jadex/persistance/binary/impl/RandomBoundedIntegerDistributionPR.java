package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.RandomBoundedIntegerDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(RandomBoundedIntegerDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getRandom()));
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());
        storeHash(object, data);
        return data;
    }

    @Override
    public RandomBoundedIntegerDistribution initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        RandomBoundedIntegerDistribution object = new RandomBoundedIntegerDistribution();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, RandomBoundedIntegerDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setRandom(manager.ensureGet(data.getLong()));
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
    }
}
