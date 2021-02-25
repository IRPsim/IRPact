package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProductPR implements Persister<BasicAdoptedProduct>, Restorer<BasicAdoptedProduct> {

    public static final BasicAdoptedProductPR INSTANCE = new BasicAdoptedProductPR();

    @Override
    public Class<BasicAdoptedProduct> getType() {
        return BasicAdoptedProduct.class;
    }

    @Override
    public Persistable persist(BasicAdoptedProduct object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getNeed()));
        data.putLong(manager.ensureGetUID(object.getProduct()));
        data.putLong(object.getTimestamp().getEpochMilli());
        return data;
    }

    @Override
    public BasicAdoptedProduct initalize(Persistable persistable) {
        return new BasicAdoptedProduct();
    }

    @Override
    public void setup(Persistable persistable, BasicAdoptedProduct object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setNeed(manager.ensureGet(data.getLong()));
        object.setProduct(manager.ensureGet(data.getLong()));
        object.setTimestamp(new BasicTimestamp(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicAdoptedProduct object, RestoreManager manager) {
    }
}
