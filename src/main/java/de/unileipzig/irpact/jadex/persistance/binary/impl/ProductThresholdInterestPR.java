package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterest;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

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

        manager.prepareAll(object.getItems().keySet());
        manager.prepareAll(object.getThresholds().keySet());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductThresholdInterest object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Double> thresholdMap = BinaryJsonData.mapToLongDoubleMap(object.getThresholds(), manager);
        data.putLongDoubleMap(thresholdMap);

        Map<Long, Double> itemMap = BinaryJsonData.mapToLongDoubleMap(object.getItems(), manager);
        data.putLongDoubleMap(itemMap);
    }

    //=========================
    //restore
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected ProductThresholdInterest doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductThresholdInterest object = new ProductThresholdInterest();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductThresholdInterest object, RestoreManager manager) throws RestoreException {
        Map<Long, Double> thresholdMap = data.getLongDoubleMap();
        for(Map.Entry<Long, Double> entry: thresholdMap.entrySet()) {
            ProductGroup group = manager.ensureGet(entry.getKey());
            object.setThreshold(group, entry.getValue());
        }

        Map<Long, Double> itemMap = data.getLongDoubleMap();
        for(Map.Entry<Long, Double> entry: itemMap.entrySet()) {
            Product product = manager.ensureGet(entry.getKey());
            object.update(product, entry.getValue());
        }
    }
}
