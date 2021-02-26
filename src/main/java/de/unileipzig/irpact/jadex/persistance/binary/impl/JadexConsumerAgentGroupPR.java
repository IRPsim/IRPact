package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
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
    public JadexConsumerAgentGroup initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        JadexConsumerAgentGroup object = new JadexConsumerAgentGroup();
        object.setName(data.getText());
        return object;
    }

    private JadexConsumerAgentGroup getOriginal(
            JadexConsumerAgentGroup object,
            RestoreManager manager) {
        SimulationEnvironment originalEnvironment = manager.getInitialInstance();
        ConsumerAgentGroup originalCag = originalEnvironment.getAgents().getConsumerAgentGroup(object.getName());
        return (JadexConsumerAgentGroup) originalCag;
    }

    private SpatialDistribution restoreSpatialDistribution(
            BinaryJsonData data,
            JadexConsumerAgentGroup object,
            RestoreManager manager) throws RestoreException {
        JadexConsumerAgentGroup original = getOriginal(object, manager);
        if(original == null) {
            throw new RestoreException("original group '" + object.getName() + "' not found");
        }

        SpatialDistribution originalDist = original.getSpatialDistribution();
        SpatialDistribution restoredDist = manager.ensureGet(data.getLong());

        if(originalDist.isShareble(restoredDist)) {
            originalDist.addComplexDataTo(restoredDist);
        } else {
            throw new RestoreException("distribution mismatch: " + originalDist.getClass() + " != " + restoredDist.getClass());
        }

        return restoredDist;
    }

    @Override
    public void setup(Persistable persistable, JadexConsumerAgentGroup object, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);

        SpatialDistribution spatialDistribution = restoreSpatialDistribution(data, object, manager);
        object.setSpatialDistribution(spatialDistribution);

        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroupAttribute[]::new));
        object.setAwarenessSupplyScheme(manager.ensureGet(data.getLong()));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, JadexConsumerAgentGroup object, RestoreManager manager) {
    }
}
