package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicVersionPR implements Persister<BasicVersion>, Restorer<BasicVersion> {

    public static final BasicVersionPR INSTANCE = new BasicVersionPR();

    @Override
    public Class<BasicVersion> getType() {
        return BasicVersion.class;
    }

    @Override
    public Persistable persist(BasicVersion object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.toString());
        return data;
    }

    @Override
    public BasicVersion initalize(Persistable persistable) {
        return new BasicVersion();
    }

    @Override
    public void setup(Persistable persistable, BasicVersion object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.set(data.getText());
    }

    @Override
    public void finalize(Persistable persistable, BasicVersion object, RestoreManager manager) {
    }
}
