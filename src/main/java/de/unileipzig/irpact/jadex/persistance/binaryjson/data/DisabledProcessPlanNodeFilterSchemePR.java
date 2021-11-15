package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.network.filter.DisabledNodeFilterScheme;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DisabledProcessPlanNodeFilterSchemePR extends BinaryPRBase<DisabledNodeFilterScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DisabledProcessPlanNodeFilterSchemePR.class);

    public static final DisabledProcessPlanNodeFilterSchemePR INSTANCE = new DisabledProcessPlanNodeFilterSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DisabledNodeFilterScheme> getType() {
        return DisabledNodeFilterScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DisabledNodeFilterScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        boolean isGlobalInstance = object == DisabledNodeFilterScheme.INSTANCE;
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
    protected DisabledNodeFilterScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        boolean isGlobalInstance = data.getBoolean();
        if(isGlobalInstance) {
            return DisabledNodeFilterScheme.INSTANCE;
        }

        DisabledNodeFilterScheme object = new DisabledNodeFilterScheme();
        object.setName(data.getText());

        return object;
    }
}
