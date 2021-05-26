package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProcessModelManagerPR extends BinaryPRBase<BasicProcessModelManager> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProcessModelManagerPR.class);

    public static final BasicProcessModelManagerPR INSTANCE = new BasicProcessModelManagerPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicProcessModelManager> getType() {
        return BasicProcessModelManager.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProcessModelManager object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        manager.prepareAll(object.getProcessModels());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProcessModelManager object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLongArray(manager.ensureGetAllUIDs(object.getProcessModels()));
    }

    //=========================
    //restore
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BasicProcessModelManager doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProcessModelManager object = new BasicProcessModelManager();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProcessModelManager object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));
        ProcessModel[] processModels = manager.ensureGetAll(data.getLongArray(), ProcessModel[]::new);
        for(ProcessModel processModel: processModels) {
            object.addProcessModel(processModel);
        }
    }
}
