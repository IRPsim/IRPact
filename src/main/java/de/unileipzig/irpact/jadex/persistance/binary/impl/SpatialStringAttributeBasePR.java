package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialStringAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttributeBasePR extends BinaryPRBase<SpatialStringAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialStringAttributeBasePR.class);

    public static final SpatialStringAttributeBasePR INSTANCE = new SpatialStringAttributeBasePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<SpatialStringAttribute> getType() {
        return SpatialStringAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(SpatialStringAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getStringValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected SpatialStringAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SpatialStringAttribute object = new SpatialStringAttribute();
        object.setName(data.getText());
        object.setStringValue(data.getText());
        return object;
    }
}
