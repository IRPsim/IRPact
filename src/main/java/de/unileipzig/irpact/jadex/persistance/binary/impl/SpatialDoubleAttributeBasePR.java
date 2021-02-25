package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.spatial.attribute.SpatialDoubleAttributeBase;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class SpatialDoubleAttributeBasePR implements Persister<SpatialDoubleAttributeBase>, Restorer<SpatialDoubleAttributeBase> {

    public static final SpatialDoubleAttributeBasePR INSTANCE = new SpatialDoubleAttributeBasePR();

    @Override
    public Class<SpatialDoubleAttributeBase> getType() {
        return SpatialDoubleAttributeBase.class;
    }

    @Override
    public Persistable persist(SpatialDoubleAttributeBase object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        return data;
    }

    @Override
    public SpatialDoubleAttributeBase initalize(Persistable persistable) {
        return new SpatialDoubleAttributeBase();
    }

    @Override
    public void setup(Persistable persistable, SpatialDoubleAttributeBase object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, SpatialDoubleAttributeBase object, RestoreManager manager) {
    }
}
