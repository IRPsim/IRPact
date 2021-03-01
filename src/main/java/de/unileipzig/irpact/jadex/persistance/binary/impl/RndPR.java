package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(Rnd object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(object.reseed());
        storeHash(object, data);
        return data;
    }

    @Override
    public Rnd initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new Rnd();
    }

    @Override
    public void setupRestore(Persistable persistable, Rnd object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setInitialSeed(data.getLong());
    }
}
