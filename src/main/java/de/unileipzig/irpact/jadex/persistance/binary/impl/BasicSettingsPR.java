package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.Version;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.develop.XXXXXXXXX;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@XXXXXXXXX("SIEHE UNTEN")
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

    @XXXXXXXXX
    @Override
    protected void doSetupPersist(BasicSettings object, BinaryJsonData data, PersistManager manager) throws PersistException {
        throw new TodoException();
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

//    private void updateSettings(XSimulationEnvironment initial, SimulationEnvironment restored) {
//        Settings initialSettings = initial.getSettings();
//        Settings restoredSettings = restored.getSettings();
//
//        restoredSettings.setLastSimulationYear(initialSettings.getLastSimulationYear());
//    }
//
//    private void applyCommandLineToEnvironment() {
//        Settings settings = environment.getSettings();
//        settings.apply(CL_OPTIONS);
//    }
}
