package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicProductPR implements Persister<BasicProduct>, Restorer<BasicProduct> {

    public static final BasicProductPR INSTANCE = new BasicProductPR();

    @Override
    public Class<BasicProduct> getType() {
        return BasicProduct.class;
    }

    @Override
    public Persistable persist(BasicProduct object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        return data;
    }

    @Override
    public BasicProduct initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProduct object = new BasicProduct();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, BasicProduct object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), ProductAttribute[]::new));
    }

    @Override
    public void finalize(Persistable persistable, BasicProduct object, RestoreManager manager) {
        object.getGroup().addProduct(object);
    }
}
