package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;

/**
 * @author Daniel Abitz
 */
public class InversePR implements Persister<Inverse>, Restorer<Inverse> {

    public static final InversePR INSTANCE = new InversePR();

    @Override
    public Class<Inverse> getType() {
        return Inverse.class;
    }

    @Override
    public Persistable persist(Inverse object, PersistManager manager) {
        return BinaryJsonPersistanceManager.initData(object, manager);
    }

    @Override
    public Inverse initalize(Persistable persistable) {
        return new Inverse();
    }

    @Override
    public void setup(Persistable persistable, Inverse object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, Inverse object, RestoreManager manager) {
    }
}
