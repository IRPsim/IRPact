package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
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

    @Override
    public Persistable initalizePersist(Inverse object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        storeHash(object, data);
        return data;
    }

    @Override
    public Inverse initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new Inverse();
    }

    @Override
    public void setupRestore(Persistable persistable, Inverse object, RestoreManager manager) {
    }
}
