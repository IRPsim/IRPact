package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.mra.component.special.DefaultHandleInterestComponent;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleInterestComponentPR extends BinaryPRBase<DefaultHandleInterestComponent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleInterestComponentPR.class);

    public static final DefaultHandleInterestComponentPR INSTANCE = new DefaultHandleInterestComponentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DefaultHandleInterestComponent> getType() {
        return DefaultHandleInterestComponent.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DefaultHandleInterestComponent object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getAdopterPoints());
        data.putInt(object.getInterestedPoints());
        data.putInt(object.getAwarePoints());
        data.putInt(object.getUnknownPoints());

        manager.prepare(object.getModel());

        return data;
    }

    @Override
    protected void doSetupPersist(DefaultHandleInterestComponent object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModel()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DefaultHandleInterestComponent doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        DefaultHandleInterestComponent object = new DefaultHandleInterestComponent();
        object.setName(data.getText());
        object.setAdopterPoints(data.getInt());
        object.setInterestedPoints(data.getInt());
        object.setAwarePoints(data.getInt());
        object.setUnknownPoints(data.getInt());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DefaultHandleInterestComponent object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setModel(manager.ensureGet(data.getLong()));
    }
}
