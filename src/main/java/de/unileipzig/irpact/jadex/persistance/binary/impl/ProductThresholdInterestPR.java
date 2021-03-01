package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterest;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestPR extends BinaryPRBase<ProductThresholdInterest> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductThresholdInterestPR.class);

    public static final ProductThresholdInterestPR INSTANCE = new ProductThresholdInterestPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProductThresholdInterest> getType() {
        return ProductThresholdInterest.class;
    }

    @Override
    public Persistable initalizePersist(ProductThresholdInterest object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.getThreshold());
        Map<Long, Double> idMap = new HashMap<>();
        for(Map.Entry<Product, Double> entry: object.getItems().entrySet()) {
            long id = manager.ensureGetUID(entry.getKey());
            idMap.put(id, entry.getValue());
        }
        data.putLongDoubleMap(idMap);
        storeHash(object, data);
        return data;
    }

    @Override
    public ProductThresholdInterest initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new ProductThresholdInterest();
    }

    @Override
    public void setupRestore(Persistable persistable, ProductThresholdInterest object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setThreshold(data.getDouble());
        Map<Long, Double> idMap = data.getLongDoubleMap();
        for(Map.Entry<Long, Double> entry: idMap.entrySet()) {
            Product product = manager.ensureGet(entry.getKey());
            object.update(product, entry.getValue());
        }
    }
}
