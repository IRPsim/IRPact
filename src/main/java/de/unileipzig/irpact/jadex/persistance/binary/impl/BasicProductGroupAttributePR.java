package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttributePR extends BinaryPRBase<BasicProductGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductGroupAttributePR.class);

    public static final BasicProductGroupAttributePR INSTANCE = new BasicProductGroupAttributePR();

    @Override
    public Class<BasicProductGroupAttribute> getType() {
        return BasicProductGroupAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProductGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getValue());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProductGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getValue()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicProductGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProductGroupAttribute object = new BasicProductGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProductGroupAttribute object, RestoreManager manager) throws RestoreException {
        object.setUnivariateDoubleDistributionValue(manager.ensureGet(data.getLong()));
    }
}
