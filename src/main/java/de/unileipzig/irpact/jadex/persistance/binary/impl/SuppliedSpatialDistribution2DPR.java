package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.twodim.SuppliedSpatialDistribution2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SuppliedSpatialDistribution2DPR extends BinaryPRBase<SuppliedSpatialDistribution2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SuppliedSpatialDistribution2DPR.class);

    public static final SuppliedSpatialDistribution2DPR INSTANCE = new SuppliedSpatialDistribution2DPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<SuppliedSpatialDistribution2D> getType() {
        return SuppliedSpatialDistribution2D.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(SuppliedSpatialDistribution2D object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getXSupplier());
        manager.prepare(object.getYSupplier());

        return data;
    }

    @Override
    protected void doSetupPersist(SuppliedSpatialDistribution2D object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getXSupplier()));
        data.putLong(manager.ensureGetUID(object.getYSupplier()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected SuppliedSpatialDistribution2D doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SuppliedSpatialDistribution2D object = new SuppliedSpatialDistribution2D();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, SuppliedSpatialDistribution2D object, RestoreManager manager) throws RestoreException {
        object.setXSupplier(manager.ensureGet(data.getLong()));
        object.setYSupplier(manager.ensureGet(data.getLong()));
    }
}
