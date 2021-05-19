package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

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
        data.putText(object.getName());
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
        Map<Long, Long> idMap = BinaryJsonData.mapToLongLongMap(
                object.getEdgeCountMap(),
                BinaryJsonData.ensureGetUID(manager),
                BinaryJsonData.INT2LONG
        );
        data.putLongLongMap(idMap);

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
        object.setName(data.getText());
        object.setInitialWeight(data.getDouble());
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        object.setSelfReferential(data.getBoolean());
        object.setAllowLessEdges(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, FreeNetworkTopology object, RestoreManager manager) throws RestoreException {
        Map<Long, Long> idMap = data.getLongLongMap();
        Map<ConsumerAgentGroup, Integer> countMap = BinaryJsonData.mapFromLongLongMap(
                idMap,
                BinaryJsonData.ensureGet(manager),
                BinaryJsonData.LONG2INT
        );
        object.setEdgeCountMap(countMap);

        object.setAffinityMapping(manager.ensureGet(data.getLong()));
        object.setDistanceEvaluator(manager.ensureGet(data.getLong()));
        object.setRnd(manager.ensureGet(data.getLong()));
    }
}
