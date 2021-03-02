package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicAdoptedProduct object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putLong(object.getTimestamp().getEpochMilli());

        manager.prepare(object.getNeed());
        manager.prepare(object.getProduct());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicAdoptedProduct object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicAdoptedProduct doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        return new BasicAdoptedProduct();
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicAdoptedProduct object, RestoreManager manager) {
        object.setTimestamp(new BasicTimestamp(data.getLong()));
        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
    }
}
