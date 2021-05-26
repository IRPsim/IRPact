package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RADataSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RADataSupplierPR extends BinaryPRBase<RADataSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RADataSupplierPR.class);

    public static final RADataSupplierPR INSTANCE = new RADataSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RADataSupplier> getType() {
        return RADataSupplier.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RADataSupplier object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(RADataSupplier object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getDistribution()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RADataSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RADataSupplier object = new RADataSupplier();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, RADataSupplier object, RestoreManager manager) throws RestoreException {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
