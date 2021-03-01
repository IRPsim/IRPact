package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicDistanceEvaluatorPR extends BinaryPRBase<BasicDistanceEvaluator> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicDistanceEvaluatorPR.class);

    public static final BasicDistanceEvaluatorPR INSTANCE = new BasicDistanceEvaluatorPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicDistanceEvaluator> getType() {
        return BasicDistanceEvaluator.class;
    }

    @Override
    public Persistable initalizePersist(BasicDistanceEvaluator object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getEval()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicDistanceEvaluator initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new BasicDistanceEvaluator();
    }

    @Override
    public void setupRestore(Persistable persistable, BasicDistanceEvaluator object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEval(manager.ensureGet(data.getLong()));
    }
}
