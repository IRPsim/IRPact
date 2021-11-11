package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessModel;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessModelPR extends BinaryPRBase<ModularRAProcessModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessModelPR.class);

    public static final ModularRAProcessModelPR INSTANCE = new ModularRAProcessModelPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ModularRAProcessModel> getType() {
        return ModularRAProcessModel.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ModularRAProcessModel object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSpeedOfConvergence());

        manager.prepare(object.getRnd());
//        manager.prepare(object.getUncertaintyManager());
//        manager.prepare(object.getRelativeAgreementAlgorithm());
        manager.prepare(object.getInterestComponent());
        manager.prepare(object.getFeasibilityComponent());
        manager.prepare(object.getDecisionMakingComponent());
        manager.prepare(object.getActionComponent());

        return data;
    }

    @Override
    protected void doSetupPersist(ModularRAProcessModel object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getRnd()));
//        data.putLong(manager.ensureGetUID(object.getUncertaintyManager()));
//        data.putLong(manager.ensureGetUID(object.getRelativeAgreementAlgorithm()));
        data.putLong(manager.ensureGetUID(object.getInterestComponent()));
        data.putLong(manager.ensureGetUID(object.getFeasibilityComponent()));
        data.putLong(manager.ensureGetUID(object.getDecisionMakingComponent()));
        data.putLong(manager.ensureGetUID(object.getActionComponent()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ModularRAProcessModel doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        ModularRAProcessModel object = new ModularRAProcessModel();
        object.setName(data.getText());
        object.setSpeedOfConvergence(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ModularRAProcessModel object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setRnd(manager.ensureGet(data.getLong()));
//        object.setUncertaintyManager(manager.ensureGet(data.getLong()));
//        object.setRelativeAgreementAlgorithm(manager.ensureGet(data.getLong()));
        object.setInterestComponent(manager.ensureGet(data.getLong()));
        object.setFeasibilityComponent(manager.ensureGet(data.getLong()));
        object.setDecisionMakingComponent(manager.ensureGet(data.getLong()));
        object.setActionComponent(manager.ensureGet(data.getLong()));
    }
}
