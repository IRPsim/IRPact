package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;

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
    protected BinaryJsonData doInitalizePersist(ProductThresholdInterestSupplyScheme object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepareAll(object.getDistributions().keySet());
        manager.prepareAll(object.getDistributions().values());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductThresholdInterestSupplyScheme object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Long> idMap = BinaryJsonData.mapToLongLongMap(object.getDistributions(), manager);
        data.putLongLongMap(idMap);
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
        Map<Long, Long> idMap = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: idMap.entrySet()) {
            ProductGroup pg = manager.ensureGet(entry.getKey());
            UnivariateDoubleDistribution dist = manager.ensureGet(entry.getValue());

            object.setThresholdDistribution(pg, dist);
        }
    }
}
