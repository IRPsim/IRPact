package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.process.filter.EntireNetworkNodeFilterScheme;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class EntireNetworkNodeFilterSchemePR extends BinaryPRBase<EntireNetworkNodeFilterScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(EntireNetworkNodeFilterSchemePR.class);

    public static final EntireNetworkNodeFilterSchemePR INSTANCE = new EntireNetworkNodeFilterSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<EntireNetworkNodeFilterScheme> getType() {
        return EntireNetworkNodeFilterScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(EntireNetworkNodeFilterScheme object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        boolean isGlobalInstance = object == EntireNetworkNodeFilterScheme.INSTANCE;
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
    protected EntireNetworkNodeFilterScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        boolean isGlobalInstance = data.getBoolean();
        if(isGlobalInstance) {
            return EntireNetworkNodeFilterScheme.INSTANCE;
        }

        EntireNetworkNodeFilterScheme object = new EntireNetworkNodeFilterScheme();
        object.setName(data.getText());

        return object;
    }
}
