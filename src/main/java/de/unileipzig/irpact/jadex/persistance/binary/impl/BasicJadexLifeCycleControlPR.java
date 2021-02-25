package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.time.BasicTimestamp;

/**
 * @author Daniel Abitz
 */
public class BasicJadexLifeCycleControlPR implements Persister<BasicJadexLifeCycleControl>, Restorer<BasicJadexLifeCycleControl> {

    public static final BasicJadexLifeCycleControlPR INSTANCE = new BasicJadexLifeCycleControlPR();

    @Override
    public Class<BasicJadexLifeCycleControl> getType() {
        return BasicJadexLifeCycleControl.class;
    }

    @Override
    public Persistable persist(BasicJadexLifeCycleControl object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        if(object.getCurrent() == null) {
            data.putNothing();
        } else {
            data.putLong(object.getCurrent().getEpochMilli());
        }
        data.putLong(manager.ensureGetUID(object.getControlAgent()));
        return data;
    }

    @Override
    public BasicJadexLifeCycleControl initalize(Persistable persistable) {
        return new BasicJadexLifeCycleControl();
    }

    @Override
    public void setup(Persistable persistable, BasicJadexLifeCycleControl object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        long epochMilli = data.getLong();
        if(epochMilli != BinaryJsonData.NOTHING_ID) {
            object.setCurrent(new BasicTimestamp(epochMilli));
        }
        object.setControlAgent(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicJadexLifeCycleControl object, RestoreManager manager) {
    }
}
