package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
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

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicJadexLifeCycleControl> getType() {
        return BasicJadexLifeCycleControl.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicJadexLifeCycleControl object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        if(object.getCurrent() == null) {
            data.putNothing();
        } else {
            data.putLong(object.getCurrent().getEpochMilli());
        }

        manager.prepare(object.getControlAgent());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicJadexLifeCycleControl object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getControlAgent()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicJadexLifeCycleControl doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        return new BasicJadexLifeCycleControl();
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicJadexLifeCycleControl object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        long epochMilli = data.getLong();
        if(epochMilli != BinaryJsonData.NOTHING_ID) {
            object.setCurrent(new BasicTimestamp(epochMilli));
        }
        object.setControlAgent(manager.ensureGet(data.getLong()));
    }
}
