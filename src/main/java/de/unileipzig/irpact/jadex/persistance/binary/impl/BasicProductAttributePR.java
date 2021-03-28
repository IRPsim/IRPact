package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.ProductDoubleAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttributePR extends BinaryPRBase<ProductDoubleAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductAttributePR.class);

    public static final BasicProductAttributePR INSTANCE = new BasicProductAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<ProductDoubleAttribute> getType() {
        return ProductDoubleAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(ProductDoubleAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(ProductDoubleAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected ProductDoubleAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProductDoubleAttribute object = new ProductDoubleAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProductDoubleAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
