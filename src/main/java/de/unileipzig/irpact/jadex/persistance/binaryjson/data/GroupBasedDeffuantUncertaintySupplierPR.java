package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.uncert.GroupBasedDeffuantUncertaintySupplier;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class GroupBasedDeffuantUncertaintySupplierPR extends BinaryPRBase<GroupBasedDeffuantUncertaintySupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GroupBasedDeffuantUncertaintySupplierPR.class);

    public static final GroupBasedDeffuantUncertaintySupplierPR INSTANCE = new GroupBasedDeffuantUncertaintySupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<GroupBasedDeffuantUncertaintySupplier> getType() {
        return GroupBasedDeffuantUncertaintySupplier.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(GroupBasedDeffuantUncertaintySupplier object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getSpeedOfConvergence());

        manager.prepare(object.getData());
        manager.prepare(object.getUncertainty());
        manager.prepare(object.getConsumerAgentGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(GroupBasedDeffuantUncertaintySupplier object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getData()));
        data.putLong(manager.ensureGetUID(object.getUncertainty()));
        data.putLong(manager.ensureGetUID(object.getConsumerAgentGroup()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected GroupBasedDeffuantUncertaintySupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        GroupBasedDeffuantUncertaintySupplier object = new GroupBasedDeffuantUncertaintySupplier();
        object.setName(data.getText());
        object.setSpeedOfConvergence(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, GroupBasedDeffuantUncertaintySupplier object, RestoreManager manager) throws RestoreException {
        object.setData(manager.ensureGet(data.getLong()));
        object.setUncertainty(manager.ensureGet(data.getLong()));
        object.setConsumerAgentGroup(manager.ensureGet(data.getLong()));
    }
}
