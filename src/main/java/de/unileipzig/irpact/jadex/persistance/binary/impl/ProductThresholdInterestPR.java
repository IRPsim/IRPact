package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterest;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ProductThresholdInterest object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putDouble(object.getThreshold());

        manager.prepareAll(object.getItems().keySet());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductThresholdInterest object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Double> idMap = new HashMap<>();
        for(Map.Entry<Product, Double> entry: object.getItems().entrySet()) {
            long id = manager.ensureGetUID(entry.getKey());
            idMap.put(id, entry.getValue());
        }
        data.putLongDoubleMap(idMap);
    }

    //=========================
    //restore
    //=========================


    @Override
    protected ProductThresholdInterest doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductThresholdInterest object = new ProductThresholdInterest();
        object.setThreshold(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductThresholdInterest object, RestoreManager manager) throws RestoreException {
        Map<Long, Double> idMap = data.getLongDoubleMap();
        for(Map.Entry<Long, Double> entry: idMap.entrySet()) {
            Product product = manager.ensureGet(entry.getKey());
            object.update(product, entry.getValue());
        }
    }
}
