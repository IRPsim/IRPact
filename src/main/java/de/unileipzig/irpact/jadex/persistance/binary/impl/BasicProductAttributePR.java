package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.BasicProductAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttributePR implements Persister<BasicProductAttribute>, Restorer<BasicProductAttribute> {

    public static final BasicProductAttributePR INSTANCE = new BasicProductAttributePR();

    @Override
    public Class<BasicProductAttribute> getType() {
        return BasicProductAttribute.class;
    }

    @Override
    public Persistable persist(BasicProductAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        return data;
    }

    @Override
    public BasicProductAttribute initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProductAttribute object = new BasicProductAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    public void setup(Persistable persistable, BasicProductAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicProductAttribute object, RestoreManager manager) {
    }
}
