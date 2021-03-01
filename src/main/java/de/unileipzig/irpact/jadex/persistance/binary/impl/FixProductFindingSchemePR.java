package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class FixProductFindingSchemePR extends BinaryPRBase<FixProductFindingScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixProductFindingSchemePR.class);

    public static final FixProductFindingSchemePR INSTANCE = new FixProductFindingSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<FixProductFindingScheme> getType() {
        return FixProductFindingScheme.class;
    }

    @Override
    public Persistable initalizePersist(FixProductFindingScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getProduct()));
        storeHash(object, data);
        return data;
    }

    @Override
    public FixProductFindingScheme initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        FixProductFindingScheme object = new FixProductFindingScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, FixProductFindingScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setProduct(manager.ensureGet(data.getLong()));
    }
}
