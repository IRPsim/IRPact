package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.uncert.DeffuantUncertainty;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DeffuantUncertaintyPR extends BinaryPRBase<DeffuantUncertainty> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DeffuantUncertaintyPR.class);

    public static final DeffuantUncertaintyPR INSTANCE = new DeffuantUncertaintyPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<DeffuantUncertainty> getType() {
        return DeffuantUncertainty.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(DeffuantUncertainty object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSpeedOfConvergence());

        manager.prepare(object.getData());

        return data;
    }

    @Override
    protected void doSetupPersist(DeffuantUncertainty object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getData()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DeffuantUncertainty doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        DeffuantUncertainty object = new DeffuantUncertainty();
        object.setName(data.getText());
        object.setSpeedOfConvergence(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DeffuantUncertainty object, RestoreManager manager) throws RestoreException {
        object.setData(manager.ensureGet(data.getLong()));
    }
}
