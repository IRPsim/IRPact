package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.awareness.ProductThresholdAwareness;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdAwarenessPR implements Persister<ProductThresholdAwareness>, Restorer<ProductThresholdAwareness> {

    public static final ProductThresholdAwarenessPR INSTANCE = new ProductThresholdAwarenessPR();

    @Override
    public Class<ProductThresholdAwareness> getType() {
        return ProductThresholdAwareness.class;
    }

    @Override
    public Persistable persist(ProductThresholdAwareness object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.getThreshold());
        Map<Long, Double> idMap = new HashMap<>();
        for(Map.Entry<Product, Double> entry: object.getItems().entrySet()) {
            long id = manager.ensureGetUID(entry.getKey());
            idMap.put(id, entry.getValue());
        }
        data.putLongDoubleMap(idMap);
        return data;
    }

    @Override
    public ProductThresholdAwareness initalize(Persistable persistable) {
        return new ProductThresholdAwareness();
    }

    @Override
    public void setup(Persistable persistable, ProductThresholdAwareness object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setThreshold(data.getDouble());
        Map<Long, Double> idMap = data.getLongDoubleMap();
        for(Map.Entry<Long, Double> entry: idMap.entrySet()) {
            Product product = manager.ensureGet(entry.getKey());
            object.update(product, entry.getValue());
        }
    }

    @Override
    public void finalize(Persistable persistable, ProductThresholdAwareness object, RestoreManager manager) {
    }
}
