package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.distribution.DiscreteSpatialDistribution;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DiscreteSpatialDistributionPR extends BinaryPRBase<DiscreteSpatialDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DiscreteSpatialDistributionPR.class);

    public static final DiscreteSpatialDistributionPR INSTANCE = new DiscreteSpatialDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DiscreteSpatialDistribution> getType() {
        return DiscreteSpatialDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DiscreteSpatialDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(DiscreteSpatialDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DiscreteSpatialDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        DiscreteSpatialDistribution object = new DiscreteSpatialDistribution();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DiscreteSpatialDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
