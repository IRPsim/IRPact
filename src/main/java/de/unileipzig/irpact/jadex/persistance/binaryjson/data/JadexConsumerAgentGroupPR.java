package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class JadexConsumerAgentGroupPR extends BinaryPRBase<JadexConsumerAgentGroup> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexConsumerAgentGroupPR.class);

    public static final JadexConsumerAgentGroupPR INSTANCE = new JadexConsumerAgentGroupPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<JadexConsumerAgentGroup> getType() {
        return JadexConsumerAgentGroup.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(JadexConsumerAgentGroup object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getInformationAuthority());
        data.putInt(object.getNextAgentId());

        manager.prepare(object.getSpatialDistribution());
        manager.prepareAll(object.getGroupAttributes());
        manager.prepareAll(object.getProductRelatedGroupAttributes());
        manager.prepare(object.getAwarenessSupplyScheme());
        manager.prepare(object.getInterestSupplyScheme());
        manager.prepare(object.getProductFindingScheme());
        manager.prepare(object.getProcessFindingScheme());

        //agents ohne speichern, identisch zu edges in graph
        for(ConsumerAgent ca: object.getAgents()) {
            manager.prepare(ca);
        }

        return data;
    }

    @Override
    protected void doSetupPersist(JadexConsumerAgentGroup object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getSpatialDistribution()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getGroupAttributes()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getProductRelatedGroupAttributes()));
        data.putLong(manager.ensureGetUID(object.getAwarenessSupplyScheme()));
        data.putLong(manager.ensureGetUID(object.getInterestSupplyScheme()));
        data.putLong(manager.ensureGetUID(object.getProductFindingScheme()));
        data.putLong(manager.ensureGetUID(object.getProcessFindingScheme()));

        //agents ohne speichern, identisch zu edges in graph
        int caCount = 0;
        for(ConsumerAgent ca: object.getAgents()) {
            manager.ensureGetUID(ca);
            caCount++;
        }
        log().trace("stores {} agents", caCount);

        //object.logChecksums();
    }

    //=========================
    //restore
    //=========================

    @Override
    protected JadexConsumerAgentGroup doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        JadexConsumerAgentGroup object = new JadexConsumerAgentGroup();
        object.setName(data.getText());
        object.setInformationAuthority(data.getDouble());
        object.setNextAgentId(data.getInt());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, JadexConsumerAgentGroup object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setSpatialDistribution(manager.ensureGet(data.getLong()));
        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroupAttribute[]::new));
        object.addAllProductRelatedGroupAttribute(manager.ensureGetAll(data.getLongArray(), ConsumerAgentProductRelatedGroupAttribute[]::new));
        object.setAwarenessSupplyScheme(manager.ensureGet(data.getLong()));
        object.setInterestSupplyScheme(manager.ensureGet(data.getLong()));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));
    }
}
