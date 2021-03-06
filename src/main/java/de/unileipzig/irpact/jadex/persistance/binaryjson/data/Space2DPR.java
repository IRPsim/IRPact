package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class Space2DPR extends BinaryPRBase<Space2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Space2DPR.class);

    public static final Space2DPR INSTANCE = new Space2DPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<Space2D> getType() {
        return Space2D.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(Space2D object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getMetric().id());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected Space2D doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        Space2D object = new Space2D();
        object.setName(data.getText());
        object.setMetric(Metric2D.get(data.getInt()));
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, Space2D object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));
    }
}
