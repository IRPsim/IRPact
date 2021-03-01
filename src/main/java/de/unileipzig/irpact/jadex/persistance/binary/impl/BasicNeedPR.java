package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicNeedPR extends BinaryPRBase<BasicNeed> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicNeedPR.class);

    public static final BasicNeedPR INSTANCE = new BasicNeedPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicNeed> getType() {
        return BasicNeed.class;
    }

    @Override
    public Persistable initalizePersist(BasicNeed object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicNeed initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicNeed object = new BasicNeed();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicNeed object, RestoreManager manager) {
    }
}
