package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ConstantUnivariateDoubleDistribution object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ConstantUnivariateDoubleDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ConstantUnivariateDoubleDistribution object = new ConstantUnivariateDoubleDistribution();
        object.setName(data.getText());
        object.setValue(data.getDouble());
        return object;
    }
}
