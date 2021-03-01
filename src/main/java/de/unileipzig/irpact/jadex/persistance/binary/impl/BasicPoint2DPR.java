package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Class<BasicPoint2D> getType() {
        return BasicPoint2D.class;
    }

    @Override
    public Persistable initalizePersist(BasicPoint2D object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.getX());
        data.putDouble(object.getY());
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicPoint2D initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicPoint2D object = new BasicPoint2D();
        object.setX(data.getDouble());
        object.setY(data.getDouble());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicPoint2D object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), SpatialAttribute[]::new));
    }
}
