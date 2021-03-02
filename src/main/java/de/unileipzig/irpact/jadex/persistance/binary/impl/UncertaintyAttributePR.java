package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.UncertaintyAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UncertaintyAttributePR extends BinaryPRBase<UncertaintyAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyAttributePR.class);

    public static final UncertaintyAttributePR INSTANCE = new UncertaintyAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UncertaintyAttribute> getType() {
        return UncertaintyAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UncertaintyAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getUncertainty());
        data.putDouble(object.getConvergence());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(UncertaintyAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getGroup()));

        System.out.println(">  " + data.getUID() + " " + data.printBytes());
        System.out.println(">> " + data.getUID() + " " + data.printMinimalJson());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UncertaintyAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UncertaintyAttribute object = new UncertaintyAttribute();
        object.setName(data.getText());
        object.setUncertainity(data.getDouble());
        object.setConvergence(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, UncertaintyAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
