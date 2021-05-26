package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.attribute.BasicProductDoubleGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttributePR extends BinaryPRBase<BasicProductDoubleGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductGroupAttributePR.class);

    public static final BasicProductGroupAttributePR INSTANCE = new BasicProductGroupAttributePR();

    @Override
    public Class<BasicProductDoubleGroupAttribute> getType() {
        return BasicProductDoubleGroupAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProductDoubleGroupAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProductDoubleGroupAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getDistribution()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicProductDoubleGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProductDoubleGroupAttribute object = new BasicProductDoubleGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProductDoubleGroupAttribute object, RestoreManager manager) throws RestoreException {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
