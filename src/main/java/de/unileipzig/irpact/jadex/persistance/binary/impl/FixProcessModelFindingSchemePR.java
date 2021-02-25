package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class FixProcessModelFindingSchemePR implements Persister<FixProcessModelFindingScheme>, Restorer<FixProcessModelFindingScheme> {

    public static final FixProcessModelFindingSchemePR INSTANCE = new FixProcessModelFindingSchemePR();

    @Override
    public Class<FixProcessModelFindingScheme> getType() {
        return FixProcessModelFindingScheme.class;
    }

    @Override
    public Persistable persist(FixProcessModelFindingScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getModel()));
        return data;
    }

    @Override
    public FixProcessModelFindingScheme initalize(Persistable persistable) {
        return new FixProcessModelFindingScheme();
    }

    @Override
    public void setup(Persistable persistable, FixProcessModelFindingScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setModel(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, FixProcessModelFindingScheme object, RestoreManager manager) {
    }
}
