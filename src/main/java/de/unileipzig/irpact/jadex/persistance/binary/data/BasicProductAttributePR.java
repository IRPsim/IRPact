package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.attribute.BasicProductDoubleAttribute;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttributePR extends BinaryPRBase<BasicProductDoubleAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductAttributePR.class);

    public static final BasicProductAttributePR INSTANCE = new BasicProductAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicProductDoubleAttribute> getType() {
        return BasicProductDoubleAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProductDoubleAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProductDoubleAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected BasicProductDoubleAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProductDoubleAttribute object = new BasicProductDoubleAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProductDoubleAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
