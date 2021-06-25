package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwareness;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProductBinaryAwarenessPR extends BinaryPRBase<ProductBinaryAwareness> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductBinaryAwarenessPR.class);

    public static final ProductBinaryAwarenessPR INSTANCE = new ProductBinaryAwarenessPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProductBinaryAwareness> getType() {
        return ProductBinaryAwareness.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ProductBinaryAwareness object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        manager.prepareAll(object.getItems());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductBinaryAwareness object, BinaryJsonData data, PersistManager manager) throws PersistException {
        long[] ids = manager.ensureGetAllUIDs(object.getItems());
        data.putLongArray(ids);
    }

    //=========================
    //restore
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected ProductBinaryAwareness doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductBinaryAwareness object = new ProductBinaryAwareness();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductBinaryAwareness object, RestoreManager manager) throws RestoreException {
        long[] ids = data.getLongArray();
        manager.ensureGetAll(ids, object.getItems());
    }
}
