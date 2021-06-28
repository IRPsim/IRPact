package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicNeedPR extends BinaryPRBase<BasicNeed> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicNeedPR.class);

    public static final BasicNeedPR INSTANCE = new BasicNeedPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicNeed> getType() {
        return BasicNeed.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicNeed object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicNeed doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicNeed object = new BasicNeed();
        object.setName(data.getText());
        return object;
    }
}
