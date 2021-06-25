package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.TruncatedNormalDistribution;
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
public class TruncatedNormalDistributionPR extends BinaryPRBase<TruncatedNormalDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(TruncatedNormalDistributionPR.class);

    public static final TruncatedNormalDistributionPR INSTANCE = new TruncatedNormalDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<TruncatedNormalDistribution> getType() {
        return TruncatedNormalDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(TruncatedNormalDistribution object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSigma());
        data.putDouble(object.getMu());
        data.putDouble(object.getSupportLowerBound());
        data.putDouble(object.getSupportUpperBound());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(TruncatedNormalDistribution object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected TruncatedNormalDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        TruncatedNormalDistribution object = new TruncatedNormalDistribution();
        object.setName(data.getText());
        object.setSigma(data.getDouble());
        object.setMu(data.getDouble());
        object.setSupportLowerBound(data.getDouble());
        object.setSupportUpperBound(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, TruncatedNormalDistribution object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));
        object.initalize();
    }
}
