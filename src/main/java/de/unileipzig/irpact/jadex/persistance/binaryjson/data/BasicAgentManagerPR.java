package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
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
        data.putLong(object.getAttentionOrderManager().peekId());

        manager.prepareAll(object.getConsumerAgentGroups());
        manager.prepare(object.getConsumerAgentGroupAffinityMapping());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicAgentManager object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putIdCollection(object.getConsumerAgentGroups(), manager.ensureGetUIDFunction());
        data.putIdValue(object.getConsumerAgentGroupAffinityMapping(), manager.ensureGetUIDFunction());
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
        object.setEnvironment(getEnvironment(manager));
        data.getIdCollection(manager.ensureGetFunction(), object::addConsumerAgentGroup);
        object.setConsumerAgentGroupAffinityMapping(data.getIdValue(manager.ensureGetFunction()));
    }
}
