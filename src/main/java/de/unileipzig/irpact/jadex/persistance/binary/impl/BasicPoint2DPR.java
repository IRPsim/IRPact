package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2DPR extends BinaryPRBase<BasicPoint2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicPoint2DPR.class);

    public static final BasicPoint2DPR INSTANCE = new BasicPoint2DPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicPoint2D> getType() {
        return BasicPoint2D.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicPoint2D object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putDouble(object.getX());
        data.putDouble(object.getY());

        manager.prepareAll(object.getAttributes());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicPoint2D object, BinaryJsonData data, PersistManager manager) {
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicPoint2D doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicPoint2D object = new BasicPoint2D();
        object.setX(data.getDouble());
        object.setY(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicPoint2D object, RestoreManager manager) throws RestoreException {
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), SpatialAttribute[]::new));
    }
}
