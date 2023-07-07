package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilterScheme;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanMaxDistanceFilterSchemePR extends BinaryPRBase<MaxDistanceNodeFilterScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlanMaxDistanceFilterSchemePR.class);

    public static final RAProcessPlanMaxDistanceFilterSchemePR INSTANCE = new RAProcessPlanMaxDistanceFilterSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<MaxDistanceNodeFilterScheme> getType() {
        return MaxDistanceNodeFilterScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(MaxDistanceNodeFilterScheme object, PersistManager manager) {
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
    protected MaxDistanceNodeFilterScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        MaxDistanceNodeFilterScheme object = new MaxDistanceNodeFilterScheme();
        object.setName(data.getText());
        object.setMaxDistance(data.getDouble());
        object.setInclusive(data.getBoolean());

        return object;
    }
}
