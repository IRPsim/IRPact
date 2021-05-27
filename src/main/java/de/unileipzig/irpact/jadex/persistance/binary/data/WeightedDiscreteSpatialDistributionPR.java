package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(WeightedDiscreteSpatialDistribution object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putLong(object.getRandom().getInitialSeed());
        data.putInt(object.getNumberOfCalls());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected WeightedDiscreteSpatialDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        WeightedDiscreteSpatialDistribution object = new WeightedDiscreteSpatialDistribution();
        object.setName(data.getText());
        object.setRandom(new Rnd(data.getLong()));
        object.setRequiredNumberOfCalls(data.getInt());
        return object;
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, WeightedDiscreteSpatialDistribution object, RestoreManager manager) {
        object.initalize();
    }
}