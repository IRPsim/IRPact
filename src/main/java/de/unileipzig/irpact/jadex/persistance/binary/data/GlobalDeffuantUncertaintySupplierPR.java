package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.uncert.GlobalDeffuantUncertaintySupplier;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class GlobalDeffuantUncertaintySupplierPR extends BinaryPRBase<GlobalDeffuantUncertaintySupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalDeffuantUncertaintySupplierPR.class);

    public static final GlobalDeffuantUncertaintySupplierPR INSTANCE = new GlobalDeffuantUncertaintySupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<GlobalDeffuantUncertaintySupplier> getType() {
        return GlobalDeffuantUncertaintySupplier.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(GlobalDeffuantUncertaintySupplier object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSpeedOfConvergence());

        manager.prepare(object.getData());
        manager.prepare(object.getUncertainty());
        manager.prepareAll(object.getConsumerAgentGroups());

        return data;
    }

    @Override
    protected void doSetupPersist(GlobalDeffuantUncertaintySupplier object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getData()));
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putIdCollection(object.getConsumerAgentGroups(), manager.ensureGetUIDFunction());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected GlobalDeffuantUncertaintySupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        GlobalDeffuantUncertaintySupplier object = new GlobalDeffuantUncertaintySupplier();
        object.setName(data.getText());
        object.setSpeedOfConvergence(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, GlobalDeffuantUncertaintySupplier object, RestoreManager manager) throws RestoreException {
        object.setData(manager.ensureGet(data.getLong()));
        object.setUncertainty(manager.ensureGet(data.getLong()));
        data.getIdCollection(manager.ensureGetFunction(), object.getConsumerAgentGroups());
    }
}
