package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class Space2DPR implements Persister<Space2D>, Restorer<Space2D> {

    public static final Space2DPR INSTANCE = new Space2DPR();

    @Override
    public Class<Space2D> getType() {
        return Space2D.class;
    }

    @Override
    public Persistable persist(Space2D object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getMetric().id());
        return data;
    }

    @Override
    public Space2D initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        Space2D object = new Space2D();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, Space2D object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setMetric(Metric2D.get(data.getInt()));
    }

    @Override
    public void finalize(Persistable persistable, Space2D object, RestoreManager manager) {
    }
}
