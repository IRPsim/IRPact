package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilter;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class MaxDistanceNodeFilterPR extends BinaryPRBase<MaxDistanceNodeFilter> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MaxDistanceNodeFilterPR.class);

    public static final MaxDistanceNodeFilterPR INSTANCE = new MaxDistanceNodeFilterPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<MaxDistanceNodeFilter> getType() {
        return MaxDistanceNodeFilter.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(MaxDistanceNodeFilter object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getMaxDistance());
        data.putBoolean(object.isInclusive());

        manager.prepare(object.getModel());
        manager.prepare(object.getOrigin());

        return data;
    }

    @Override
    protected void doSetupPersist(MaxDistanceNodeFilter object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModel()));
        data.putLong(manager.ensureGetUID(object.getOrigin()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected MaxDistanceNodeFilter doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        MaxDistanceNodeFilter object = new MaxDistanceNodeFilter();
        object.setName(data.getText());
        object.setMaxDistance(data.getDouble());
        object.setInclusive(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, MaxDistanceNodeFilter object, RestoreManager manager) throws RestoreException {
        object.setModel(manager.ensureGet(data.getLong()));
        object.setOrigin(manager.ensureGet(data.getLong()));
    }
}
