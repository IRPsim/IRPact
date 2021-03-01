package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttributePR extends BinaryPRBase<BasicProductAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductAttributePR.class);

    public static final BasicProductAttributePR INSTANCE = new BasicProductAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicProductAttribute> getType() {
        return BasicProductAttribute.class;
    }

    @Override
    public Persistable initalizePersist(BasicProductAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicProductAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProductAttribute object = new BasicProductAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicProductAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
