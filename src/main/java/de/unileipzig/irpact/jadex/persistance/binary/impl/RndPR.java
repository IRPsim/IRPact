package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class RndPR implements Persister<Rnd>, Restorer<Rnd> {

    public static final RndPR INSTANCE = new RndPR();

    @Override
    public Class<Rnd> getType() {
        return Rnd.class;
    }

    @Override
    public Persistable persist(Rnd object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(object.reseed());
        return data;
    }

    @Override
    public Rnd initalize(Persistable persistable, RestoreManager manager) {
        return new Rnd();
    }

    @Override
    public void setup(Persistable persistable, Rnd object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setInitialSeed(data.getLong());
    }

    @Override
    public void finalize(Persistable persistable, Rnd object, RestoreManager manager) {
    }
}
