package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.mra.component.special.DefaultHandleFeasibilityComponent;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleFeasibilityComponentPR extends BinaryPRBase<DefaultHandleFeasibilityComponent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleFeasibilityComponentPR.class);

    public static final DefaultHandleFeasibilityComponentPR INSTANCE = new DefaultHandleFeasibilityComponentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DefaultHandleFeasibilityComponent> getType() {
        return DefaultHandleFeasibilityComponent.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DefaultHandleFeasibilityComponent object, PersistManager manager) throws PersistException {
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
    protected void doSetupPersist(DefaultHandleFeasibilityComponent object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModel()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DefaultHandleFeasibilityComponent doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        DefaultHandleFeasibilityComponent object = new DefaultHandleFeasibilityComponent();
        object.setName(data.getText());
        object.setAdopterPoints(data.getInt());
        object.setInterestedPoints(data.getInt());
        object.setAwarePoints(data.getInt());
        object.setUnknownPoints(data.getInt());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DefaultHandleFeasibilityComponent object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setModel(manager.ensureGet(data.getLong()));
    }
}
