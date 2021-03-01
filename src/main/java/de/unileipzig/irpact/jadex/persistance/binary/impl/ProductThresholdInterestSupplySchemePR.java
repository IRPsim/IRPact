package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(ProductThresholdInterestSupplyScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getDistribution()));
        storeHash(object, data);
        return data;
    }

    @Override
    public ProductThresholdInterestSupplyScheme initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        ProductThresholdInterestSupplyScheme object = new ProductThresholdInterestSupplyScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, ProductThresholdInterestSupplyScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
