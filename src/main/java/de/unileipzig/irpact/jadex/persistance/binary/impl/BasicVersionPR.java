package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.UncheckedParsingException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.start.IRPact;

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
    public BasicVersion initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicVersion object = new BasicVersion();
        object.set(data.getText());

        if(IRPact.VERSION.isMismatch(object)) {
            throw new UncheckedParsingException("version mismatch! IRPact version: '" + IRPact.VERSION + "', input version: '" + object + "'");
        }
        return object;
    }

    @Override
    public void setup(Persistable persistable, BasicVersion object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, BasicVersion object, RestoreManager manager) {
    }
}
