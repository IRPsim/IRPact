package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.RandomBoundedIntegerDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistributionPR implements Persister<RandomBoundedIntegerDistribution>, Restorer<RandomBoundedIntegerDistribution> {

    public static final RandomBoundedIntegerDistributionPR INSTANCE = new RandomBoundedIntegerDistributionPR();

    @Override
    public Class<RandomBoundedIntegerDistribution> getType() {
        return RandomBoundedIntegerDistribution.class;
    }

    @Override
    public Persistable persist(RandomBoundedIntegerDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getRandom()));
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());
        return data;
    }

    @Override
    public RandomBoundedIntegerDistribution initalize(Persistable persistable) {
        return new RandomBoundedIntegerDistribution();
    }

    @Override
    public void setup(Persistable persistable, RandomBoundedIntegerDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setRandom(manager.ensureGet(data.getLong()));
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, RandomBoundedIntegerDistribution object, RestoreManager manager) {
    }
}
