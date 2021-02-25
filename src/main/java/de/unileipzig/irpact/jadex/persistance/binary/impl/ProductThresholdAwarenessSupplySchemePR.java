package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.awareness.ProductThresholdAwarenessSupplyScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdAwarenessSupplySchemePR implements Persister<ProductThresholdAwarenessSupplyScheme>, Restorer<ProductThresholdAwarenessSupplyScheme> {

    public static final ProductThresholdAwarenessSupplySchemePR INSTANCE = new ProductThresholdAwarenessSupplySchemePR();

    @Override
    public Class<ProductThresholdAwarenessSupplyScheme> getType() {
        return ProductThresholdAwarenessSupplyScheme.class;
    }

    @Override
    public Persistable persist(ProductThresholdAwarenessSupplyScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getDistribution()));
        return data;
    }

    @Override
    public ProductThresholdAwarenessSupplyScheme initalize(Persistable persistable) {
        return new ProductThresholdAwarenessSupplyScheme();
    }

    @Override
    public void setup(Persistable persistable, ProductThresholdAwarenessSupplyScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setDistribution(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, ProductThresholdAwarenessSupplyScheme object, RestoreManager manager) {
    }
}
