package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2DPR implements Persister<BasicPoint2D>, Restorer<BasicPoint2D> {

    public static final BasicPoint2DPR INSTANCE = new BasicPoint2DPR();

    @Override
    public Class<BasicPoint2D> getType() {
        return BasicPoint2D.class;
    }

    @Override
    public Persistable persist(BasicPoint2D object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putDouble(object.getX());
        data.putDouble(object.getY());
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        return data;
    }

    @Override
    public BasicPoint2D initalize(Persistable persistable) {
        return new BasicPoint2D();
    }

    @Override
    public void setup(Persistable persistable, BasicPoint2D object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setX(data.getDouble());
        object.setY(data.getDouble());
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), SpatialAttribute[]::new));
    }

    @Override
    public void finalize(Persistable persistable, BasicPoint2D object, RestoreManager manager) {
    }
}
