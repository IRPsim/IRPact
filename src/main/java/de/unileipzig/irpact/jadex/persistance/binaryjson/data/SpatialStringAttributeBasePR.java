package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttributeBasePR extends BinaryPRBase<BasicSpatialStringAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialStringAttributeBasePR.class);

    public static final SpatialStringAttributeBasePR INSTANCE = new SpatialStringAttributeBasePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicSpatialStringAttribute> getType() {
        return BasicSpatialStringAttribute.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicSpatialStringAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getStringValue());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicSpatialStringAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicSpatialStringAttribute object = new BasicSpatialStringAttribute();
        object.setName(data.getText());
        object.setStringValue(data.getText());
        return object;
    }
}
