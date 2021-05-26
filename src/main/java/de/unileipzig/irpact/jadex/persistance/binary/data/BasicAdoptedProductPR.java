package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProductPR extends BinaryPRBase<BasicAdoptedProduct> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAdoptedProductPR.class);

    public static final BasicAdoptedProductPR INSTANCE = new BasicAdoptedProductPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicAdoptedProduct> getType() {
        return BasicAdoptedProduct.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicAdoptedProduct object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putBoolean(object.isInitial());

        if(object.isInitial()) {
            data.putNothing();
        } else {
            data.putLong(object.getTimestamp().getEpochMilli());
            manager.prepare(object.getNeed());
        }

        manager.prepare(object.getProduct());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicAdoptedProduct object, BinaryJsonData data, PersistManager manager) throws PersistException {
        if(object.isInitial()) {
            data.putNothing();
        } else {
            data.putLong(manager.ensureGetUID(object.getNeed()));
        }
        data.putLong(manager.ensureGetUID(object.getProduct()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicAdoptedProduct doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicAdoptedProduct object = new BasicAdoptedProduct();
        object.setInitial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicAdoptedProduct object, RestoreManager manager) throws RestoreException {
        long millis = data.getLong();
        long needId = data.getLong();
        if(object.isInitial()) {
            object.setTimestamp(null);
            object.setNeed(null);
        } else {
            object.setTimestamp(new BasicTimestamp(millis));
            object.setNeed(manager.ensureGet(needId));
        }
        object.setProduct(manager.ensureGet(data.getLong()));
    }
}
