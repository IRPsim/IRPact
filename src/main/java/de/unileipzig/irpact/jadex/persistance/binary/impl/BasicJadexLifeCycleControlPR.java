package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicJadexLifeCycleControlPR extends BinaryPRBase<BasicJadexLifeCycleControl> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControlPR.class);

    public static final BasicJadexLifeCycleControlPR INSTANCE = new BasicJadexLifeCycleControlPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicJadexLifeCycleControl> getType() {
        return BasicJadexLifeCycleControl.class;
    }

    @Override
    public Persistable initalizePersist(BasicJadexLifeCycleControl object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        if(object.getCurrent() == null) {
            data.putNothing();
        } else {
            data.putLong(object.getCurrent().getEpochMilli());
        }
        data.putLong(manager.ensureGetUID(object.getControlAgent()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicJadexLifeCycleControl initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new BasicJadexLifeCycleControl();
    }

    @Override
    public void setupRestore(Persistable persistable, BasicJadexLifeCycleControl object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(JadexSimulationEnvironment.class));

        BasicJadexSimulationEnvironment initial = manager.getInitialInstance();
        setupKillSwitch(initial, object);

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        long epochMilli = data.getLong();
        if(epochMilli != BinaryJsonData.NOTHING_ID) {
            object.setCurrent(new BasicTimestamp(epochMilli));
        }
        object.setControlAgent(manager.ensureGet(data.getLong()));
    }

    private void setupKillSwitch(BasicJadexSimulationEnvironment initialEnv, BasicJadexLifeCycleControl restored) {
        BasicJadexLifeCycleControl initial = (BasicJadexLifeCycleControl) initialEnv.getLiveCycleControl();
        restored.setKillSwitchTimeout(initial.getKillSwitchTimeout());
    }
}
