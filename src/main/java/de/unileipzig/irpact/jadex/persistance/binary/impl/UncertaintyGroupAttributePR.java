package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyGroupAttributeOLD;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributePR extends BinaryPRBase<UncertaintyGroupAttributeOLD> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyGroupAttributePR.class);

    public static final UncertaintyGroupAttributePR INSTANCE = new UncertaintyGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UncertaintyGroupAttributeOLD> getType() {
        return UncertaintyGroupAttributeOLD.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UncertaintyGroupAttributeOLD object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getUncertainty());
        manager.prepare(object.getConvergence());

        return data;
    }

    @Override
    protected void doSetupPersist(UncertaintyGroupAttributeOLD object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConvergence()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UncertaintyGroupAttributeOLD doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UncertaintyGroupAttributeOLD object = new UncertaintyGroupAttributeOLD();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, UncertaintyGroupAttributeOLD object, RestoreManager manager) throws RestoreException {
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConvergence(manager.ensureGet(data.getLong()));
    }
}
