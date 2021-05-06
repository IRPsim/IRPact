package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.filter.RAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanMaxDistanceFilterSchemePR extends BinaryPRBase<RAProcessPlanMaxDistanceFilterScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlanMaxDistanceFilterSchemePR.class);

    public static final RAProcessPlanMaxDistanceFilterSchemePR INSTANCE = new RAProcessPlanMaxDistanceFilterSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<RAProcessPlanMaxDistanceFilterScheme> getType() {
        return RAProcessPlanMaxDistanceFilterScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(RAProcessPlanMaxDistanceFilterScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getMaxDistance());
        data.putBoolean(object.isInclusive());

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected RAProcessPlanMaxDistanceFilterScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        RAProcessPlanMaxDistanceFilterScheme object = new RAProcessPlanMaxDistanceFilterScheme();
        object.setName(data.getText());
        object.setMaxDistance(data.getDouble());
        object.setInclusive(data.getBoolean());

        return object;
    }
}
