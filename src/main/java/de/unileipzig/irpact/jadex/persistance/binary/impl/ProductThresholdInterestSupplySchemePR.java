package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplySchemePR implements Persister<ProductThresholdInterestSupplyScheme>, Restorer<ProductThresholdInterestSupplyScheme> {

    public static final ProductThresholdInterestSupplySchemePR INSTANCE = new ProductThresholdInterestSupplySchemePR();

    @Override
    public Class<ProductThresholdInterestSupplyScheme> getType() {
        return ProductThresholdInterestSupplyScheme.class;
    }

    @Override
    public Persistable persist(ProductThresholdInterestSupplyScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getDistribution()));
        return data;
    }

    @Override
    public ProductThresholdInterestSupplyScheme initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        ProductThresholdInterestSupplyScheme object = new ProductThresholdInterestSupplyScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, ProductThresholdInterestSupplyScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, ProductThresholdInterestSupplyScheme object, RestoreManager manager) {
    }
}
