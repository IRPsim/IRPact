package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.attributes.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyGroupAttributeSupplierPR extends BinaryPRBase<BasicUncertaintyGroupAttributeSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyGroupAttributeSupplierPR.class);

    public static final BasicUncertaintyGroupAttributeSupplierPR INSTANCE = new BasicUncertaintyGroupAttributeSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicUncertaintyGroupAttributeSupplier> getType() {
        return BasicUncertaintyGroupAttributeSupplier.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicUncertaintyGroupAttributeSupplier object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getAttributeName());

        manager.prepare(object.getUncertaintyDistribution());
        if(object.hasConvergenceDistribution()) {
            manager.prepare(object.getConvergenceDistribution());
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicUncertaintyGroupAttributeSupplier object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getUncertaintyDistribution()));
        if(object.hasConvergenceDistribution()) {
            data.putLong(manager.ensureGetUID(object.getConvergenceDistribution()));
        } else {
            data.putNothing();
        }
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicUncertaintyGroupAttributeSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicUncertaintyGroupAttributeSupplier object = new BasicUncertaintyGroupAttributeSupplier();
        object.setName(data.getText());
        object.setAttributeName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicUncertaintyGroupAttributeSupplier object, RestoreManager manager) throws RestoreException {
        object.setUncertaintyDistribution(manager.ensureGet(data.getLong()));
        long convId = data.getLong();
        if(convId != BinaryJsonData.NOTHING_ID) {
            object.setConvergenceDistribution(manager.ensureGet(convId));
        }
    }
}
