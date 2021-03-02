package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialDoubleAttributeBase;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpatialDoubleAttributeBasePR extends BinaryPRBase<SpatialDoubleAttributeBase> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialDoubleAttributeBasePR.class);

    public static final SpatialDoubleAttributeBasePR INSTANCE = new SpatialDoubleAttributeBasePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<SpatialDoubleAttributeBase> getType() {
        return SpatialDoubleAttributeBase.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(SpatialDoubleAttributeBase object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected SpatialDoubleAttributeBase doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SpatialDoubleAttributeBase object = new SpatialDoubleAttributeBase();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }
}
