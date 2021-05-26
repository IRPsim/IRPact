package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RndPR extends BinaryPRBase<Rnd> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RndPR.class);

    public static final RndPR INSTANCE = new RndPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<Rnd> getType() {
        return Rnd.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(Rnd object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putLong(object.reseed());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected Rnd doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        Rnd object = new Rnd();
        object.setInitialSeed(data.getLong());
        return object;
    }
}
