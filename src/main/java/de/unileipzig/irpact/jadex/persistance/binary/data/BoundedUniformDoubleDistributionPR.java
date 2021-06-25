package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.BoundedUniformDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BoundedUniformDoubleDistributionPR extends BinaryPRBase<BoundedUniformDoubleDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BoundedUniformDoubleDistributionPR.class);

    public static final BoundedUniformDoubleDistributionPR INSTANCE = new BoundedUniformDoubleDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BoundedUniformDoubleDistribution> getType() {
        return BoundedUniformDoubleDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BoundedUniformDoubleDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BoundedUniformDoubleDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BoundedUniformDoubleDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BoundedUniformDoubleDistribution object = new BoundedUniformDoubleDistribution();
        object.setName(data.getText());
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BoundedUniformDoubleDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
