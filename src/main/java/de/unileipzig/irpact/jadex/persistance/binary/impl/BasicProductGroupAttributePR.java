package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttributePR extends BinaryPRBase<BasicProductGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductGroupAttributePR.class);

    public static final BasicProductGroupAttributePR INSTANCE = new BasicProductGroupAttributePR();

    @Override
    public Class<BasicProductGroupAttribute> getType() {
        return BasicProductGroupAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Persistable initalizePersist(BasicProductGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getValue()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicProductGroupAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProductGroupAttribute object = new BasicProductGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicProductGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
