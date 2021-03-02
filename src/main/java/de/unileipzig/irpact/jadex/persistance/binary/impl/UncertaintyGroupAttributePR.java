package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.UncertaintyGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributePR extends BinaryPRBase<UncertaintyGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyGroupAttributePR.class);

    public static final UncertaintyGroupAttributePR INSTANCE = new UncertaintyGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UncertaintyGroupAttribute> getType() {
        return UncertaintyGroupAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UncertaintyGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getUncertainty());
        manager.prepare(object.getConvergence());

        return data;
    }

    @Override
    protected void doSetupPersist(UncertaintyGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConvergence()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UncertaintyGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UncertaintyGroupAttribute object = new UncertaintyGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, UncertaintyGroupAttribute object, RestoreManager manager) throws RestoreException {
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConvergence(manager.ensureGet(data.getLong()));
    }
}
