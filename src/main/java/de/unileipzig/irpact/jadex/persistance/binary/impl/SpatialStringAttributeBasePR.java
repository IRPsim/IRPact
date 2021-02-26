package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttributeBase;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttributeBasePR implements Persister<SpatialStringAttributeBase>, Restorer<SpatialStringAttributeBase> {

    public static final SpatialStringAttributeBasePR INSTANCE = new SpatialStringAttributeBasePR();

    @Override
    public Class<SpatialStringAttributeBase> getType() {
        return SpatialStringAttributeBase.class;
    }

    @Override
    public Persistable persist(SpatialStringAttributeBase object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getStringValue());
        return data;
    }

    @Override
    public SpatialStringAttributeBase initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        SpatialStringAttributeBase object = new SpatialStringAttributeBase();
        object.setName(data.getText());
        object.setStringValue(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, SpatialStringAttributeBase object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, SpatialStringAttributeBase object, RestoreManager manager) {
    }
}
