package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopologyPR extends BinaryPRBase<FreeNetworkTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopologyPR.class);

    public static final FreeNetworkTopologyPR INSTANCE = new FreeNetworkTopologyPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<FreeNetworkTopology> getType() {
        return FreeNetworkTopology.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(FreeNetworkTopology object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putDouble(object.getInitialWeight());
        data.putInt(object.getEdgeType().id());
        data.putBoolean(object.isSelfReferential());
        data.putBoolean(object.isAllowLessEdges());

        for(Map.Entry<ConsumerAgentGroup, Integer> entry: object.getEdgeCountMap().entrySet()) {
            manager.prepare(entry.getKey());
        }

        manager.prepare(object.getAffinityMapping());
        manager.prepare(object.getDistanceEvaluator());
        manager.prepare(object.getRnd());

        return data;
    }

    @Override
    protected void doSetupPersist(FreeNetworkTopology object, BinaryJsonData data, PersistManager manager) throws PersistException {
        Map<Long, Long> map = new LinkedHashMap<>();
        for(Map.Entry<ConsumerAgentGroup, Integer> entry: object.getEdgeCountMap().entrySet()) {
            long uid = manager.ensureGetUID(entry.getKey());
            map.put(uid, entry.getValue().longValue());
        }
        data.putLongLongMap(map);

        data.putLong(manager.ensureGetUID(object.getAffinityMapping()));
        data.putLong(manager.ensureGetUID(object.getDistanceEvaluator()));
        data.putLong(manager.ensureGetUID(object.getRnd()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected FreeNetworkTopology doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        FreeNetworkTopology object = new FreeNetworkTopology();
        object.setInitialWeight(data.getDouble());
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        object.setSelfReferential(data.getBoolean());
        object.setAllowLessEdges(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, FreeNetworkTopology object, RestoreManager manager) throws RestoreException {
        Map<ConsumerAgentGroup, Integer> countMap = new LinkedHashMap<>();
        Map<Long, Long> map = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
            countMap.put(cag, entry.getValue().intValue());
        }
        object.setEdgeCountMap(countMap);

        object.setAffinityMapping(manager.ensureGet(data.getLong()));
        object.setDistanceEvaluator(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
    }
}
