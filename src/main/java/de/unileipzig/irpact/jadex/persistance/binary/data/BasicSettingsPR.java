package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicSettingsPR extends BinaryPRBase<BasicSettings> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSettingsPR.class);

    public static final BasicSettingsPR INSTANCE = new BasicSettingsPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicSettings> getType() {
        return BasicSettings.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicSettings object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getCurrentRun());
        data.putInt(object.getLastSimulationYear());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicSettings object, BinaryJsonData data, PersistManager manager) throws PersistException {
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicSettings doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicSettings object = new BasicSettings();
        object.setNumberOfPreviousRuns(data.getInt());
        object.setLastSimulationYearOfPreviousRun(data.getInt());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicSettings object, RestoreManager manager) throws RestoreException {
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, BasicSettings object, RestoreManager manager) {
    }
}
