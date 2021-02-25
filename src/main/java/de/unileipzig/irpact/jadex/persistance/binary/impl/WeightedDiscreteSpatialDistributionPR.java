package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistributionPR implements Persister<WeightedDiscreteSpatialDistribution>, Restorer<WeightedDiscreteSpatialDistribution> {

    public static final WeightedDiscreteSpatialDistributionPR INSTANCE = new WeightedDiscreteSpatialDistributionPR();

    @Override
    public Class<WeightedDiscreteSpatialDistribution> getType() {
        return WeightedDiscreteSpatialDistribution.class;
    }

    @Override
    public Persistable persist(WeightedDiscreteSpatialDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(object.getRandom().getInitialSeed());
        data.putInt(object.getNummberOfCalls());
        return data;
    }

    @Override
    public WeightedDiscreteSpatialDistribution initalize(Persistable persistable) {
        return new WeightedDiscreteSpatialDistribution();
    }

    @Override
    public void setup(Persistable persistable, WeightedDiscreteSpatialDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setRandom(new Rnd(data.getLong()));
        object.setRequiredNumberOfCalls(data.getInt());
    }

    @Override
    public void finalize(Persistable persistable, WeightedDiscreteSpatialDistribution object, RestoreManager manager) {
    }
}
