package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class NoDistancePR extends BinaryPRBase<NoDistance> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(NoDistancePR.class);

    public static final NoDistancePR INSTANCE = new NoDistancePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<NoDistance> getType() {
        return NoDistance.class;
    }

    //=========================
    //persist
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BinaryJsonData doInitalizePersist(NoDistance object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected NoDistance doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        return new NoDistance();
    }
}
