package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductManagerPR extends BinaryPRBase<BasicProductManager> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductManagerPR.class);

    public static final BasicProductManagerPR INSTANCE = new BasicProductManagerPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicProductManager> getType() {
        return BasicProductManager.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProductManager object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        manager.prepareAll(object.getGroups());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProductManager object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLongArray(manager.ensureGetAllUIDs(object.getGroups()));
    }

    //=========================
    //restore
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BasicProductManager doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProductManager object = new BasicProductManager();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProductManager object, RestoreManager manager) throws RestoreException {
        ProductGroup[] productGroups = manager.ensureGetAll(data.getLongArray(), ProductGroup[]::new);
        for(ProductGroup productGroup: productGroups) {
            object.add(productGroup);
        }
    }
}
