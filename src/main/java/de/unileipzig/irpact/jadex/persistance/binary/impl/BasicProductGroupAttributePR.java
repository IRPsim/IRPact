package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttributePR implements Persister<BasicProductGroupAttribute>, Restorer<BasicProductGroupAttribute> {

    public static final BasicProductGroupAttributePR INSTANCE = new BasicProductGroupAttributePR();

    @Override
    public Class<BasicProductGroupAttribute> getType() {
        return BasicProductGroupAttribute.class;
    }

    @Override
    public Persistable persist(BasicProductGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getValue()));
        return data;
    }

    @Override
    public BasicProductGroupAttribute initalize(Persistable persistable) {
        return new BasicProductGroupAttribute();
    }

    @Override
    public void setup(Persistable persistable, BasicProductGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setDistribution(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicProductGroupAttribute object, RestoreManager manager) {
    }
}
