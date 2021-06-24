package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.uncert.GlobalDeffuantUncertaintyData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class GlobalDeffuantUncertaintyDataPR extends BinaryPRBase<GlobalDeffuantUncertaintyData> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalDeffuantUncertaintyDataPR.class);

    public static final GlobalDeffuantUncertaintyDataPR INSTANCE = new GlobalDeffuantUncertaintyDataPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<GlobalDeffuantUncertaintyData> getType() {
        return GlobalDeffuantUncertaintyData.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(GlobalDeffuantUncertaintyData object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getExtremistParameter());
        data.putDouble(object.getExtremistUncertainty());
        data.putDouble(object.getModerateUncertainty());
        data.putBoolean(object.isLowerBoundInclusive());
        data.putBoolean(object.isUpperBoundInclusive());

        manager.prepare(object.getEnvironment());
        manager.prepareAll(object.getRanges().values());

        return data;
    }

    @Override
    protected void doSetupPersist(GlobalDeffuantUncertaintyData object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getEnvironment()));
        data.putIdMapWithStringKey(object.getRanges(), manager.ensureGetUIDFunction());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected GlobalDeffuantUncertaintyData doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        GlobalDeffuantUncertaintyData object = new GlobalDeffuantUncertaintyData();
        object.setName(data.getText());
        object.setExtremistParameter(data.getDouble());
        object.setExtremistUncertainty(data.getDouble());
        object.setModerateUncertainty(data.getDouble());
        object.setLowerBoundInclusive(data.getBoolean());
        object.setUpperBoundInclusive(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, GlobalDeffuantUncertaintyData object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGet(data.getLong()));
        data.getIdMapWithStringKey(manager.ensureGetFunction(), object.getRanges());
    }
}
