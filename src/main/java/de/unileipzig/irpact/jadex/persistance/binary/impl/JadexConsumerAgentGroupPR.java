package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(JadexConsumerAgentGroup object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    @Override
    protected void doSetupPersist(JadexConsumerAgentGroup object, Persistable persistable, PersistManager manager) {
        BinaryJsonData data = check(persistable);
        data.putDouble(object.getInformationAuthority());
        data.putInt(object.getNextAgentId());

        data.putLong(manager.ensureGetUID(object.getSpatialDistribution()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        data.putLong(manager.ensureGetUID(object.getAwarenessSupplyScheme()));
        data.putLong(manager.ensureGetUID(object.getProductFindingScheme()));
        data.putLong(manager.ensureGetUID(object.getProcessFindingScheme()));

        storeHash(object, data);

        //agents ohne speichern, identisch zu edges in graph
        int caCount = 0;
        for(ConsumerAgent ca: object.getAgents()) {
            manager.ensureGetUID(ca);
            caCount++;
        }
        log().debug("stores {} agents", caCount);
    }

    @Override
    public JadexConsumerAgentGroup initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        JadexConsumerAgentGroup object = new JadexConsumerAgentGroup();
        object.setName(data.getText());
        object.setInformationAuthority(data.getDouble());
        object.setNextAgentId(data.getInt());

        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, JadexConsumerAgentGroup object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);

        SpatialDistribution spatialDistribution = restoreSpatialDistribution(data, object, manager);
        object.setSpatialDistribution(spatialDistribution);

        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroupAttribute[]::new));
        object.setAwarenessSupplyScheme(manager.ensureGet(data.getLong()));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));
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
}
