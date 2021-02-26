package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicDistanceEvaluatorPR implements Persister<BasicDistanceEvaluator>, Restorer<BasicDistanceEvaluator> {

    public static final BasicDistanceEvaluatorPR INSTANCE = new BasicDistanceEvaluatorPR();

    @Override
    public Class<BasicDistanceEvaluator> getType() {
        return BasicDistanceEvaluator.class;
    }

    @Override
    public Persistable persist(BasicDistanceEvaluator object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getEval()));
        return data;
    }

    @Override
    public BasicDistanceEvaluator initalize(Persistable persistable, RestoreManager manager) {
        return new BasicDistanceEvaluator();
    }

    @Override
    public void setup(Persistable persistable, BasicDistanceEvaluator object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEval(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicDistanceEvaluator object, RestoreManager manager) {
    }
}
