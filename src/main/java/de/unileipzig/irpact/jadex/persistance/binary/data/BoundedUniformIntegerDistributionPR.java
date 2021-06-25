package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.BoundedUniformIntegerDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BoundedUniformIntegerDistributionPR extends BinaryPRBase<BoundedUniformIntegerDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BoundedUniformIntegerDistributionPR.class);

    public static final BoundedUniformIntegerDistributionPR INSTANCE = new BoundedUniformIntegerDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BoundedUniformIntegerDistribution> getType() {
        return BoundedUniformIntegerDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BoundedUniformIntegerDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BoundedUniformIntegerDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BoundedUniformIntegerDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BoundedUniformIntegerDistribution object = new BoundedUniformIntegerDistribution();
        object.setName(data.getText());
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BoundedUniformIntegerDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
