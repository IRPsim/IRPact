package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(BasicProduct object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicProduct initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProduct object = new BasicProduct();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicProduct object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), ProductAttribute[]::new));
    }

    @Override
    public void finalizeRestore(Persistable persistable, BasicProduct object, RestoreManager manager) {
        object.getGroup().addProduct(object);
    }
}
