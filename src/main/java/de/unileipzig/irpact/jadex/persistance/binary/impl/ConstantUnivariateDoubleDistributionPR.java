package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDoubleDistributionPR implements Persister<ConstantUnivariateDoubleDistribution>, Restorer<ConstantUnivariateDoubleDistribution> {

    public static final ConstantUnivariateDoubleDistributionPR INSTANCE = new ConstantUnivariateDoubleDistributionPR();

    @Override
    public Class<ConstantUnivariateDoubleDistribution> getType() {
        return ConstantUnivariateDoubleDistribution.class;
    }

    @Override
    public Persistable persist(ConstantUnivariateDoubleDistribution object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getValue());
        return data;
    }

    @Override
    public ConstantUnivariateDoubleDistribution initalize(Persistable persistable) {
        return new ConstantUnivariateDoubleDistribution();
    }

    @Override
    public void setup(Persistable persistable, ConstantUnivariateDoubleDistribution object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setValue(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, ConstantUnivariateDoubleDistribution object, RestoreManager manager) {
    }
}
