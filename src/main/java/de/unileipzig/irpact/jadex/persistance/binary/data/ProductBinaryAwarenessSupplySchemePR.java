package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProductBinaryAwarenessSupplySchemePR extends BinaryPRBase<ProductBinaryAwarenessSupplyScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductBinaryAwarenessSupplySchemePR.class);

    public static final ProductBinaryAwarenessSupplySchemePR INSTANCE = new ProductBinaryAwarenessSupplySchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProductBinaryAwarenessSupplyScheme> getType() {
        return ProductBinaryAwarenessSupplyScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ProductBinaryAwarenessSupplyScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductBinaryAwarenessSupplyScheme object, BinaryJsonData data, PersistManager manager) {
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ProductBinaryAwarenessSupplyScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductBinaryAwarenessSupplyScheme object = new ProductBinaryAwarenessSupplyScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductBinaryAwarenessSupplyScheme object, RestoreManager manager) throws RestoreException {
    }
}
