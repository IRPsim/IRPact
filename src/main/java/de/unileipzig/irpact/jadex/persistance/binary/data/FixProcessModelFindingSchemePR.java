package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class FixProcessModelFindingSchemePR extends BinaryPRBase<FixProcessModelFindingScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixProcessModelFindingSchemePR.class);

    public static final FixProcessModelFindingSchemePR INSTANCE = new FixProcessModelFindingSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<FixProcessModelFindingScheme> getType() {
        return FixProcessModelFindingScheme.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(FixProcessModelFindingScheme object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getModel());

        return data;
    }

    @Override
    protected void doSetupPersist(FixProcessModelFindingScheme object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModel()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected FixProcessModelFindingScheme doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        FixProcessModelFindingScheme object = new FixProcessModelFindingScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, FixProcessModelFindingScheme object, RestoreManager manager) throws RestoreException {
        object.setModel(manager.ensureGet(data.getLong()));
    }
}
