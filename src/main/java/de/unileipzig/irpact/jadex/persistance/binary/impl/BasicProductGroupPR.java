package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupPR implements Persister<BasicProductGroup>, Restorer<BasicProductGroup> {

    public static final BasicProductGroupPR INSTANCE = new BasicProductGroupPR();

    @Override
    public Class<BasicProductGroup> getType() {
        return BasicProductGroup.class;
    }

    @Override
    public Persistable persist(BasicProductGroup object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLongArray(manager.ensureGetAllUIDs(object.getGroupAttributes()));
        return data;
    }

    @Override
    public BasicProductGroup initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProductGroup object = new BasicProductGroup();
        object.setName(data.getText());
        return object;
    }

    /*
     * Products werden ad hoch rekonstruiet
     */
    @Override
    public void setup(Persistable persistable, BasicProductGroup object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ProductGroupAttribute[]::new));
    }

    @Override
    public void finalize(Persistable persistable, BasicProductGroup object, RestoreManager manager) {
    }
}
