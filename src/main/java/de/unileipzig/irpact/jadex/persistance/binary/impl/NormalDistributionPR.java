package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.NormalDistribution;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class NormalDistributionPR extends BinaryPRBase<NormalDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(NormalDistributionPR.class);

    public static final NormalDistributionPR INSTANCE = new NormalDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<NormalDistribution> getType() {
        return NormalDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(NormalDistribution object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getStandardDeviation());
        data.putDouble(object.getMean());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(NormalDistribution object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected NormalDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        NormalDistribution object = new NormalDistribution();
        object.setName(data.getText());
        object.setStandardDeviation(data.getDouble());
        object.setMean(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, NormalDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
