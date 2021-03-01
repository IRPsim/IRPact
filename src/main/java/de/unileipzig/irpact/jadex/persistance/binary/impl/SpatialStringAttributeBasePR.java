package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttributeBase;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttributeBasePR extends BinaryPRBase<SpatialStringAttributeBase> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialStringAttributeBasePR.class);

    public static final SpatialStringAttributeBasePR INSTANCE = new SpatialStringAttributeBasePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<SpatialStringAttributeBase> getType() {
        return SpatialStringAttributeBase.class;
    }

    @Override
    public Persistable initalizePersist(SpatialStringAttributeBase object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getStringValue());
        storeHash(object, data);
        return data;
    }

    @Override
    public SpatialStringAttributeBase initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        SpatialStringAttributeBase object = new SpatialStringAttributeBase();
        object.setName(data.getText());
        object.setStringValue(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, SpatialStringAttributeBase object, RestoreManager manager) {
    }
}
