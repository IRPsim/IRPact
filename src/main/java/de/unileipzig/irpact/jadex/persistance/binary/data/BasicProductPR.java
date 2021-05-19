package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductPR extends BinaryPRBase<BasicProduct> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductPR.class);

    public static final BasicProductPR INSTANCE = new BasicProductPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicProduct> getType() {
        return BasicProduct.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProduct object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getGroup());
        manager.prepareAll(object.getAttributes());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProduct object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicProduct doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProduct object = new BasicProduct();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProduct object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.setGroup(manager.ensureGet(data.getLong()));
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), ProductAttribute[]::new));
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, BasicProduct object, RestoreManager manager) {
        object.getGroup().addProduct(object);
    }
}
