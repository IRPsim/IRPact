package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.spatial.ConstantSpatialDistribution;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class ConstantSpatialDistributionPR implements Persister<ConstantSpatialDistribution>, Restorer<ConstantSpatialDistribution> {

    public static final ConstantSpatialDistributionPR INSTANCE = new ConstantSpatialDistributionPR();

    @Override
    public Class<ConstantSpatialDistribution> getType() {
        return ConstantSpatialDistribution.class;
    }

    @Override
    public Persistable persist(ConstantSpatialDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getConstantInformation()));
        return data;
    }

    @Override
    public ConstantSpatialDistribution initalize(Persistable persistable) {
        return new ConstantSpatialDistribution();
    }

    @Override
    public void setup(Persistable persistable, ConstantSpatialDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setConstantInformation(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, ConstantSpatialDistribution object, RestoreManager manager) {
    }
}
