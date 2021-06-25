package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.BoundedNormalDistribution;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BoundedNormalDistributionPR extends BinaryPRBase<BoundedNormalDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BoundedNormalDistributionPR.class);

    public static final BoundedNormalDistributionPR INSTANCE = new BoundedNormalDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BoundedNormalDistribution> getType() {
        return BoundedNormalDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BoundedNormalDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getStandardDeviation());
        data.putDouble(object.getMean());
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BoundedNormalDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BoundedNormalDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BoundedNormalDistribution object = new BoundedNormalDistribution();
        object.setName(data.getText());
        object.setStandardDeviation(data.getDouble());
        object.setMean(data.getDouble());
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BoundedNormalDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
