package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.filter.DisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DisabledProcessPlanNodeFilterSchemePR extends BinaryPRBase<DisabledProcessPlanNodeFilterScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DisabledProcessPlanNodeFilterSchemePR.class);

    public static final DisabledProcessPlanNodeFilterSchemePR INSTANCE = new DisabledProcessPlanNodeFilterSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DisabledProcessPlanNodeFilterScheme> getType() {
        return DisabledProcessPlanNodeFilterScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DisabledProcessPlanNodeFilterScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        boolean isGlobalInstance = object == DisabledProcessPlanNodeFilterScheme.INSTANCE;
        data.putBoolean(isGlobalInstance);

        if(!isGlobalInstance) {
            data.putText(object.getName());
        }

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DisabledProcessPlanNodeFilterScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        boolean isGlobalInstance = data.getBoolean();
        if(isGlobalInstance) {
            return DisabledProcessPlanNodeFilterScheme.INSTANCE;
        }

        DisabledProcessPlanNodeFilterScheme object = new DisabledProcessPlanNodeFilterScheme();
        object.setName(data.getText());

        return object;
    }
}
