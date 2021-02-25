package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class JadexConsumerAgentGroupPR implements Persister<JadexConsumerAgentGroup>, Restorer<JadexConsumerAgentGroup> {

    public static final JadexConsumerAgentGroupPR INSTANCE = new JadexConsumerAgentGroupPR();

    @Override
    public Class<JadexConsumerAgentGroup> getType() {
        return JadexConsumerAgentGroup.class;
    }

    @Override
    public Persistable persist(JadexConsumerAgentGroup object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getSpatialDistribution()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        data.putLong(manager.ensureGetUID(object.getAwarenessSupplyScheme()));
        data.putLong(manager.ensureGetUID(object.getProductFindingScheme()));
        data.putLong(manager.ensureGetUID(object.getProcessFindingScheme()));
        return data;
    }

    @Override
    public JadexConsumerAgentGroup initalize(Persistable persistable) {
        return new JadexConsumerAgentGroup();
    }

    @Override
    public void setup(Persistable persistable, JadexConsumerAgentGroup object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setSpatialDistribution(manager.ensureGet(data.getLong()));
        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroupAttribute[]::new));
        object.setAwarenessSupplyScheme(manager.ensureGet(data.getLong()));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, JadexConsumerAgentGroup object, RestoreManager manager) {
    }
}
