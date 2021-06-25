package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.filter.EntireNetworkNodeFilter;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class EntireNetworkNodeFilterPR extends BinaryPRBase<EntireNetworkNodeFilter> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(EntireNetworkNodeFilterPR.class);

    public static final EntireNetworkNodeFilterPR INSTANCE = new EntireNetworkNodeFilterPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<EntireNetworkNodeFilter> getType() {
        return EntireNetworkNodeFilter.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(EntireNetworkNodeFilter object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        boolean isGlobalInstance = object == EntireNetworkNodeFilter.INSTANCE;
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
    protected EntireNetworkNodeFilter doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        boolean isGlobalInstance = data.getBoolean();
        if(isGlobalInstance) {
            return EntireNetworkNodeFilter.INSTANCE;
        }

        EntireNetworkNodeFilter object = new EntireNetworkNodeFilter();
        object.setName(data.getText());

        return object;
    }
}
