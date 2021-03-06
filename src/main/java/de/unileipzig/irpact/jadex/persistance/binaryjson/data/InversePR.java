package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class InversePR extends BinaryPRBase<Inverse> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InversePR.class);

    public static final InversePR INSTANCE = new InversePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<Inverse> getType() {
        return Inverse.class;
    }

    //=========================
    //persist
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BinaryJsonData doInitalizePersist(Inverse object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected Inverse doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        return new Inverse();
    }
}
