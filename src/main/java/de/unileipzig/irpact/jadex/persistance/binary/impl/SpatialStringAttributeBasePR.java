package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttributeBase;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(SpatialStringAttributeBase object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getStringValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected SpatialStringAttributeBase doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SpatialStringAttributeBase object = new SpatialStringAttributeBase();
        object.setName(data.getText());
        object.setStringValue(data.getText());
        return object;
    }
}
