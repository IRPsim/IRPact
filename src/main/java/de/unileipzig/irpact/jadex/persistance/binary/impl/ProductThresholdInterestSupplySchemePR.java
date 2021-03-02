package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplySchemePR extends BinaryPRBase<ProductThresholdInterestSupplyScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductThresholdInterestSupplySchemePR.class);

    public static final ProductThresholdInterestSupplySchemePR INSTANCE = new ProductThresholdInterestSupplySchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProductThresholdInterestSupplyScheme> getType() {
        return ProductThresholdInterestSupplyScheme.class;
    }

    //=========================
    //persist
    //=========================


    @Override
    protected BinaryJsonData doInitalizePersist(ProductThresholdInterestSupplyScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductThresholdInterestSupplyScheme object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getDistribution()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ProductThresholdInterestSupplyScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductThresholdInterestSupplyScheme object = new ProductThresholdInterestSupplyScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductThresholdInterestSupplyScheme object, RestoreManager manager) throws RestoreException {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
