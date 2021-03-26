package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.attributes.LinkedUncertaintyGroupAttribute;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyAttributeOLD;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class LinkedUncertaintyGroupAttributePR extends BinaryPRBase<LinkedUncertaintyGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LinkedUncertaintyGroupAttributePR.class);

    public static final LinkedUncertaintyGroupAttributePR INSTANCE = new LinkedUncertaintyGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<LinkedUncertaintyGroupAttribute> getType() {
        return LinkedUncertaintyGroupAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(LinkedUncertaintyGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getUncertainty());
        manager.prepare(object.getConvergence());

        return data;
    }

    @Override
    protected void doSetupPersist(LinkedUncertaintyGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConvergence()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected LinkedUncertaintyGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        LinkedUncertaintyGroupAttribute object = new LinkedUncertaintyGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, LinkedUncertaintyGroupAttribute object, RestoreManager manager) throws RestoreException {
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConvergence(manager.ensureGet(data.getLong()));
    }
}
