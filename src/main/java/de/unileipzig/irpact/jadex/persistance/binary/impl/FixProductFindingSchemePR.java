package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class FixProductFindingSchemePR implements Persister<FixProductFindingScheme>, Restorer<FixProductFindingScheme> {

    public static final FixProductFindingSchemePR INSTANCE = new FixProductFindingSchemePR();

    @Override
    public Class<FixProductFindingScheme> getType() {
        return FixProductFindingScheme.class;
    }

    @Override
    public Persistable persist(FixProductFindingScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getProduct()));
        return data;
    }

    @Override
    public FixProductFindingScheme initalize(Persistable persistable) {
        return new FixProductFindingScheme();
    }

    @Override
    public void setup(Persistable persistable, FixProductFindingScheme object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setProduct(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, FixProductFindingScheme object, RestoreManager manager) {
    }
}
