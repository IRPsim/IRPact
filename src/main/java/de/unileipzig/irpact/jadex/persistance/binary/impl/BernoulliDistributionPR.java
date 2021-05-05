package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.BernoulliDistribution;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BernoulliDistributionPR extends BinaryPRBase<BernoulliDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BernoulliDistributionPR.class);

    public static final BernoulliDistributionPR INSTANCE = new BernoulliDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BernoulliDistribution> getType() {
        return BernoulliDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BernoulliDistribution object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getP());
        data.putDouble(object.getFalseValue());
        data.putDouble(object.getTrueValue());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BernoulliDistribution object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BernoulliDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BernoulliDistribution object = new BernoulliDistribution();
        object.setName(data.getText());
        object.setP(data.getDouble());
        object.setFalseValue(data.getDouble());
        object.setTrueValue(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BernoulliDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
