package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.LinkedHashMap;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentGroupAffinityMapping object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        for(ConsumerAgentGroup src: object.sources()) {
            manager.prepare(src);
            ConsumerAgentGroupAffinities aff = object.get(src);
            for(ConsumerAgentGroup tar: aff.targets()) {
                manager.prepare(tar);
            }
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentGroupAffinityMapping object, BinaryJsonData data, PersistManager manager) throws PersistException {
        Map<Long, Map<Long, Double>> table = new LinkedHashMap<>();
        for(ConsumerAgentGroup src: object.sources()) {
            long srcUid = manager.ensureGetUID(src);
            ConsumerAgentGroupAffinities aff = object.get(src);
            for(ConsumerAgentGroup tar: aff.targets()) {
                long tarUid = manager.ensureGetUID(tar);
                double value = aff.getValue(tar);
                Map<Long, Double> m = table.computeIfAbsent(srcUid, _srcUid -> new LinkedHashMap<>());
                m.put(tarUid, value);
            }
        }
        data.putLongLongDoubleMap(table);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentGroupAffinityMapping doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentGroupAffinityMapping object = new BasicConsumerAgentGroupAffinityMapping();
        object.setName(data.getText());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentGroupAffinityMapping object, RestoreManager manager) throws RestoreException {
        Map<Long, Map<Long, Double>> table = data.getLongLongDoubleTable();
        for(Map.Entry<Long, Map<Long, Double>> srcEntry: table.entrySet()) {
            ConsumerAgentGroup srcCag = manager.ensureGet(srcEntry.getKey());
            for(Map.Entry<Long, Double> tarEntry: srcEntry.getValue().entrySet()) {
                ConsumerAgentGroup tarCag = manager.ensureGet(tarEntry.getKey());
                double value = tarEntry.getValue();
                object.put(srcCag, tarCag, value);
            }
        }
        getAgentManager(manager).setConsumerAgentGroupAffinityMapping(object);
    }
}
