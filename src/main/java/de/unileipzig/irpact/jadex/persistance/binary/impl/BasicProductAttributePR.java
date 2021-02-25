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
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putDouble(object.getDoubleValue());
        return data;
    }

    @Override
    public BasicProductAttribute initalize(Persistable persistable) {
        return new BasicProductAttribute();
    }

    @Override
    public void setup(Persistable persistable, BasicProductAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setGroup(manager.ensureGet(data.getLong()));
        object.setDoubleValue(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, BasicProductAttribute object, RestoreManager manager) {
    }
}
