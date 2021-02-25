package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionPR implements Persister<BooleanDistribution>, Restorer<BooleanDistribution> {

    public static final BooleanDistributionPR INSTANCE = new BooleanDistributionPR();

    @Override
    public Class<BooleanDistribution> getType() {
        return BooleanDistribution.class;
    }

    @Override
    public Persistable persist(BooleanDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getRandom()));
        return data;
    }

    @Override
    public BooleanDistribution initalize(Persistable persistable) {
        return new BooleanDistribution();
    }

    @Override
    public void setup(Persistable persistable, BooleanDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setRandom(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BooleanDistribution object, RestoreManager manager) {
    }
}
