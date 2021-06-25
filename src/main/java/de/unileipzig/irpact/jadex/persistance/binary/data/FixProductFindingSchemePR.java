package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.product.FixProductFindingScheme;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(FixProductFindingScheme object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getProduct());

        return data;
    }

    @Override
    protected void doSetupPersist(FixProductFindingScheme object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getProduct()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected FixProductFindingScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        FixProductFindingScheme object = new FixProductFindingScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, FixProductFindingScheme object, RestoreManager manager) throws RestoreException {
        object.setProduct(manager.ensureGet(data.getLong()));
    }
}
