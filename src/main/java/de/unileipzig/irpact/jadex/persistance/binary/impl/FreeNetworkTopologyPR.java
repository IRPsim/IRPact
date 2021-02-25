package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopologyPR implements Persister<FreeNetworkTopology>, Restorer<FreeNetworkTopology> {

    public static final FreeNetworkTopologyPR INSTANCE = new FreeNetworkTopologyPR();

    @Override
    public Class<FreeNetworkTopology> getType() {
        return FreeNetworkTopology.class;
    }

    @Override
    public Persistable persist(FreeNetworkTopology object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putInt(object.getEdgeType().id());

        Map<Long, Long> map = new HashMap<>();
        for(Map.Entry<ConsumerAgentGroup, Integer> entry: object.getEdgeCountMap().entrySet()) {
            long uid = manager.ensureGetUID(entry.getKey());
            map.put(uid, entry.getValue().longValue());
        }
        data.putLongLongMap(map);

        data.putLong(manager.ensureGetUID(object.getAffinityMapping()));
        data.putLong(manager.ensureGetUID(object.getDistanceEvaluator()));
        data.putDouble(object.getInitialWeight());
        data.putLong(manager.ensureGetUID(object.getRnd()));
        return data;
    }

    @Override
    public FreeNetworkTopology initalize(Persistable persistable) {
        return new FreeNetworkTopology();
    }

    @Override
    public void setup(Persistable persistable, FreeNetworkTopology object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));

        Map<ConsumerAgentGroup, Integer> countMap = new HashMap<>();
        Map<Long, Long> map = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
            countMap.put(cag, entry.getValue().intValue());
        }
        object.setEdgeCountMap(countMap);

        object.setAffinityMapping(manager.ensureGet(data.getLong()));
        object.setDistanceEvaluator(manager.ensureGet(data.getLong()));
        object.setInitialWeight(data.getDouble());
        object.setRnd(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, FreeNetworkTopology object, RestoreManager manager) {
    }
}
