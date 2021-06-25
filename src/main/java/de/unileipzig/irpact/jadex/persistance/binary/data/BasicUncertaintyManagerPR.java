package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.uncert.BasicUncertaintyManager;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyManagerPR extends BinaryPRBase<BasicUncertaintyManager> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyManagerPR.class);

    public static final BasicUncertaintyManagerPR INSTANCE = new BasicUncertaintyManagerPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicUncertaintyManager> getType() {
        return BasicUncertaintyManager.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicUncertaintyManager object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepareAll(object.getSuppliers());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicUncertaintyManager object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putIdCollection(object.getSuppliers(), manager.ensureGetUIDFunction());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicUncertaintyManager doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicUncertaintyManager object = new BasicUncertaintyManager();
        object.setName(data.getText());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicUncertaintyManager object, RestoreManager manager) throws RestoreException {
        data.getIdCollection(manager.ensureGetFunction(), object.getSuppliers());
    }
}
