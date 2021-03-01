package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
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

    @Override
    public Persistable initalizePersist(NoDistance object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        storeHash(object, data);
        return data;
    }

    @Override
    public NoDistance initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new NoDistance();
    }

    @Override
    public void setupRestore(Persistable persistable, NoDistance object, RestoreManager manager) {
    }
}
