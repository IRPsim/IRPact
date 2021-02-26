package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ra.UncertaintyAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class UncertaintyAttributePR implements Persister<UncertaintyAttribute>, Restorer<UncertaintyAttribute> {

    public static final UncertaintyAttributePR INSTANCE = new UncertaintyAttributePR();

    @Override
    public Class<UncertaintyAttribute> getType() {
        return UncertaintyAttribute.class;
    }

    @Override
    public Persistable persist(UncertaintyAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putDouble(object.getUncertainty());
        data.putDouble(object.getConvergence());
        return data;
    }

    @Override
    public UncertaintyAttribute initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        UncertaintyAttribute object = new UncertaintyAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, UncertaintyAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
        object.setUncertainity(data.getDouble());
        object.setConvergence(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, UncertaintyAttribute object, RestoreManager manager) {
    }
}
