package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.attributes.BasicUncertaintyAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyAttributePR extends BinaryPRBase<BasicUncertaintyAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyAttributePR.class);

    public static final BasicUncertaintyAttributePR INSTANCE = new BasicUncertaintyAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicUncertaintyAttribute> getType() {
        return BasicUncertaintyAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicUncertaintyAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getUncertainty());
        data.putDouble(object.getConvergence());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicUncertaintyAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicUncertaintyAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicUncertaintyAttribute object = new BasicUncertaintyAttribute();
        object.setName(data.getText());
        object.setUncertainity(data.getDouble());
        object.setConvergence(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicUncertaintyAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
