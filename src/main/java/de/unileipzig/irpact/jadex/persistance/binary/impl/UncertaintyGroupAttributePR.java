package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.UncertaintyGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributePR extends BinaryPRBase<UncertaintyGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyGroupAttributePR.class);

    public static final UncertaintyGroupAttributePR INSTANCE = new UncertaintyGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UncertaintyGroupAttribute> getType() {
        return UncertaintyGroupAttribute.class;
    }

    @Override
    public Persistable initalizePersist(UncertaintyGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConvergence()));
        storeHash(object, data);
        return data;
    }

    @Override
    public UncertaintyGroupAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        UncertaintyGroupAttribute object = new UncertaintyGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, UncertaintyGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConvergence(manager.ensureGet(data.getLong()));
    }
}
