package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionPR extends BinaryPRBase<BooleanDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BooleanDistributionPR.class);

    public static final BooleanDistributionPR INSTANCE = new BooleanDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BooleanDistribution> getType() {
        return BooleanDistribution.class;
    }

    @Override
    public Persistable initalizePersist(BooleanDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getRandom()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BooleanDistribution initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BooleanDistribution object = new BooleanDistribution();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BooleanDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
