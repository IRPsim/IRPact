package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinityMappingPR extends BinaryPRBase<BasicConsumerAgentGroupAffinityMapping> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAffinityMappingPR.class);

    public static final BasicConsumerAgentGroupAffinityMappingPR INSTANCE = new BasicConsumerAgentGroupAffinityMappingPR();

    @Override
    public Class<BasicConsumerAgentGroupAffinityMapping> getType() {
        return BasicConsumerAgentGroupAffinityMapping.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Persistable initalizePersist(BasicConsumerAgentGroupAffinityMapping object, PersistManager manager) {
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
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicConsumerAgentGroupAffinityMapping initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new BasicConsumerAgentGroupAffinityMapping();
    }

    @Override
    public void setupRestore(Persistable persistable, BasicConsumerAgentGroupAffinityMapping object, RestoreManager manager) {
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
}
