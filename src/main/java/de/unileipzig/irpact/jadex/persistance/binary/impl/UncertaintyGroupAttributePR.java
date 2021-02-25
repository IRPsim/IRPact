package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ra.UncertaintyGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributePR implements Persister<UncertaintyGroupAttribute>, Restorer<UncertaintyGroupAttribute> {

    public static final UncertaintyGroupAttributePR INSTANCE = new UncertaintyGroupAttributePR();

    @Override
    public Class<UncertaintyGroupAttribute> getType() {
        return UncertaintyGroupAttribute.class;
    }

    @Override
    public Persistable persist(UncertaintyGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConvergence()));
        return data;
    }

    @Override
    public UncertaintyGroupAttribute initalize(Persistable persistable) {
        return new UncertaintyGroupAttribute();
    }

    @Override
    public void setup(Persistable persistable, UncertaintyGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConvergence(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, UncertaintyGroupAttribute object, RestoreManager manager) {
    }
}
