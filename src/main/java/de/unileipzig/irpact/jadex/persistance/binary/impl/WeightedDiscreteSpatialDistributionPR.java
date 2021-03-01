package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistributionPR extends BinaryPRBase<WeightedDiscreteSpatialDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WeightedDiscreteSpatialDistributionPR.class);

    public static final WeightedDiscreteSpatialDistributionPR INSTANCE = new WeightedDiscreteSpatialDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<WeightedDiscreteSpatialDistribution> getType() {
        return WeightedDiscreteSpatialDistribution.class;
    }

    @Override
    public Persistable initalizePersist(WeightedDiscreteSpatialDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(object.getRandom().getInitialSeed());
        data.putInt(object.getNumberOfCalls());
        storeHash(object, data);
        return data;
    }

    @Override
    public WeightedDiscreteSpatialDistribution initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        WeightedDiscreteSpatialDistribution object = new WeightedDiscreteSpatialDistribution();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, WeightedDiscreteSpatialDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setRandom(new Rnd(data.getLong()));
        object.setRequiredNumberOfCalls(data.getInt());
    }

    @Override
    public void finalizeRestore(Persistable persistable, WeightedDiscreteSpatialDistribution object, RestoreManager manager) {
        object.initalize();
    }
}
