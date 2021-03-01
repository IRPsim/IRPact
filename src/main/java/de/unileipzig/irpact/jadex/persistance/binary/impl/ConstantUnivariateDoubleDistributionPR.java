package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDoubleDistributionPR extends BinaryPRBase<ConstantUnivariateDoubleDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConstantUnivariateDoubleDistributionPR.class);

    public static final ConstantUnivariateDoubleDistributionPR INSTANCE = new ConstantUnivariateDoubleDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ConstantUnivariateDoubleDistribution> getType() {
        return ConstantUnivariateDoubleDistribution.class;
    }

    @Override
    public Persistable initalizePersist(ConstantUnivariateDoubleDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getValue());
        storeHash(object, data);
        return data;
    }

    @Override
    public ConstantUnivariateDoubleDistribution initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        ConstantUnivariateDoubleDistribution object = new ConstantUnivariateDoubleDistribution();
        object.setName(data.getText());
        object.setValue(data.getDouble());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, ConstantUnivariateDoubleDistribution object, RestoreManager manager) {
    }
}
