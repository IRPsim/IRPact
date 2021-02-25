package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicNeedPR implements Persister<BasicNeed>, Restorer<BasicNeed> {

    public static final BasicNeedPR INSTANCE = new BasicNeedPR();

    @Override
    public Class<BasicNeed> getType() {
        return BasicNeed.class;
    }

    @Override
    public Persistable persist(BasicNeed object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    @Override
    public BasicNeed initalize(Persistable persistable) {
        return new BasicNeed();
    }

    @Override
    public void setup(Persistable persistable, BasicNeed object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
    }

    @Override
    public void finalize(Persistable persistable, BasicNeed object, RestoreManager manager) {
    }
}
