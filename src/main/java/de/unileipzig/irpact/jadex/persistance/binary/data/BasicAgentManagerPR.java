package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicAgentManagerPR extends BinaryPRBase<BasicAgentManager> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAgentManagerPR.class);

    public static final BasicAgentManagerPR INSTANCE = new BasicAgentManagerPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicAgentManager> getType() {
        return BasicAgentManager.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicAgentManager object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putLong(object.getAttentionOrderManager().lastId());

        manager.prepareAll(object.getConsumerAgentGroups());
        manager.prepare(object.getConsumerAgentGroupAffinityMapping());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicAgentManager object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLongArray(manager.ensureGetAllUIDs(object.getConsumerAgentGroups()));
        data.putLong(manager.ensureGetUID(object.getConsumerAgentGroupAffinityMapping()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicAgentManager doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicAgentManager object = new BasicAgentManager();
        object.getAttentionOrderManager().reset(data.getLong());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicAgentManager object, RestoreManager manager) throws RestoreException {
        ConsumerAgentGroup[] cags = manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroup[]::new);
        for(ConsumerAgentGroup cag: cags) {
            object.addConsumerAgentGroup(cag);
        }
        object.setConsumerAgentGroupAffinityMapping(manager.ensureGet(data.getLong()));
    }
}
