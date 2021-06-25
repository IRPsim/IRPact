package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.DiracUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DiracUnivariateDoubleDistributionPR extends BinaryPRBase<DiracUnivariateDoubleDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DiracUnivariateDoubleDistributionPR.class);

    public static final DiracUnivariateDoubleDistributionPR INSTANCE = new DiracUnivariateDoubleDistributionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DiracUnivariateDoubleDistribution> getType() {
        return DiracUnivariateDoubleDistribution.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DiracUnivariateDoubleDistribution object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DiracUnivariateDoubleDistribution doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        DiracUnivariateDoubleDistribution object = new DiracUnivariateDoubleDistribution();
        object.setName(data.getText());
        object.setValue(data.getDouble());
        return object;
    }
}
