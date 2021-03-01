package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProductPR extends BinaryPRBase<BasicAdoptedProduct> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAdoptedProductPR.class);

    public static final BasicAdoptedProductPR INSTANCE = new BasicAdoptedProductPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicAdoptedProduct> getType() {
        return BasicAdoptedProduct.class;
    }

    @Override
    public Persistable initalizePersist(BasicAdoptedProduct object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
        data.putLong(object.getTimestamp().getEpochMilli());
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicAdoptedProduct initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new BasicAdoptedProduct();
    }

    @Override
    public void setupRestore(Persistable persistable, BasicAdoptedProduct object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
        object.setTimestamp(new BasicTimestamp(data.getLong()));
    }
}
