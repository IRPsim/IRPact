package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinityMappingPR implements Persister<BasicConsumerAgentGroupAffinityMapping>, Restorer<BasicConsumerAgentGroupAffinityMapping> {

    public static final BasicConsumerAgentGroupAffinityMappingPR INSTANCE = new BasicConsumerAgentGroupAffinityMappingPR();

    @Override
    public Class<BasicConsumerAgentGroupAffinityMapping> getType() {
        return BasicConsumerAgentGroupAffinityMapping.class;
    }

    @Override
    public Persistable persist(BasicConsumerAgentGroupAffinityMapping object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        Map<Long, Map<Long, Double>> table = new HashMap<>();
        for(ConsumerAgentGroup src: object.sources()) {
            long srcUid = manager.ensureGetUID(src);
            ConsumerAgentGroupAffinities aff = object.get(src);
            for(ConsumerAgentGroup tar: aff.targets()) {
                long tarUid = manager.ensureGetUID(tar);
                double value = aff.getValue(tar);
                Map<Long, Double> m = table.computeIfAbsent(srcUid, _srcUid -> new HashMap<>());
                m.put(tarUid, value);
            }
        }
        data.putLongLongDoubleTable(table);
        return data;
    }

    @Override
    public BasicConsumerAgentGroupAffinityMapping initalize(Persistable persistable, RestoreManager manager) {
        return new BasicConsumerAgentGroupAffinityMapping();
    }

    @Override
    public void setup(Persistable persistable, BasicConsumerAgentGroupAffinityMapping object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        Map<Long, Map<Long, Double>> table = data.getLongLongDoubleTable();
        for(Map.Entry<Long, Map<Long, Double>> srcEntry: table.entrySet()) {
            ConsumerAgentGroup srcCag = manager.ensureGet(srcEntry.getKey());
            for(Map.Entry<Long, Double> tarEntry: srcEntry.getValue().entrySet()) {
                ConsumerAgentGroup tarCag = manager.ensureGet(tarEntry.getKey());
                double value = tarEntry.getValue();
                object.put(srcCag, tarCag, value);
            }
        }
    }

    @Override
    public void finalize(Persistable persistable, BasicConsumerAgentGroupAffinityMapping object, RestoreManager manager) {
    }
}
