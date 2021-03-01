package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ra.UncertaintyAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UncertaintyAttributePR extends BinaryPRBase<UncertaintyAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyAttributePR.class);

    public static final UncertaintyAttributePR INSTANCE = new UncertaintyAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UncertaintyAttribute> getType() {
        return UncertaintyAttribute.class;
    }

    @Override
    public Persistable initalizePersist(UncertaintyAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putDouble(object.getUncertainty());
        data.putDouble(object.getConvergence());
        storeHash(object, data);
        return data;
    }

    @Override
    public UncertaintyAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        UncertaintyAttribute object = new UncertaintyAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, UncertaintyAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
        object.setUncertainity(data.getDouble());
        object.setConvergence(data.getDouble());
    }
}
