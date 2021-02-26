package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;

/**
 * @author Daniel Abitz
 */
public class NoDistancePR implements Persister<NoDistance>, Restorer<NoDistance> {

    public static final NoDistancePR INSTANCE = new NoDistancePR();

    @Override
    public Class<NoDistance> getType() {
        return NoDistance.class;
    }

    @Override
    public Persistable persist(NoDistance object, PersistManager manager) {
        return BinaryJsonPersistanceManager.initData(object, manager);
    }

    @Override
    public NoDistance initalize(Persistable persistable, RestoreManager manager) {
        return new NoDistance();
    }

    @Override
    public void setup(Persistable persistable, NoDistance object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, NoDistance object, RestoreManager manager) {
    }
}
