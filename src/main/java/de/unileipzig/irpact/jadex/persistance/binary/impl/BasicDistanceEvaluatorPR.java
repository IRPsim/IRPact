package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicDistanceEvaluator> getType() {
        return BasicDistanceEvaluator.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicDistanceEvaluator object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getEval()));
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicDistanceEvaluator doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        return new BasicDistanceEvaluator();
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicDistanceEvaluator object, RestoreManager manager) {
        object.setEval(manager.ensureGet(data.getLong()));
    }
}
