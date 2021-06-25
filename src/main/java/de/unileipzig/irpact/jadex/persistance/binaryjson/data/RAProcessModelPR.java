package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessModelPR extends BinaryPRBase<RAProcessModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModelPR.class);

    public static final RAProcessModelPR INSTANCE = new RAProcessModelPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAProcessModel> getType() {
        return RAProcessModel.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RAProcessModel object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSpeedOfConvergence());

        manager.prepare(object.getModelData());
        manager.prepare(object.getRnd());
        manager.prepare(object.getUncertaintyManager());
        manager.prepare(object.getRelativeAgreementAlgorithm());
        manager.prepare(object.getNodeFilterScheme());

        return data;
    }

    @Override
    protected void doSetupPersist(RAProcessModel object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModelData()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
        data.putLong(manager.ensureGetUID(object.getUncertaintyManager()));
        data.putLong(manager.ensureGetUID(object.getRelativeAgreementAlgorithm()));
        data.putLong(manager.ensureGetUID(object.getNodeFilterScheme()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RAProcessModel doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        RAProcessModel object = new RAProcessModel();
        object.setName(data.getText());
        object.setSpeedOfConvergence(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, RAProcessModel object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setModelData(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
        object.setUncertaintyManager(manager.ensureGet(data.getLong()));
        object.setRelativeAgreementAlgorithm(manager.ensureGet(data.getLong()));
    }
}
