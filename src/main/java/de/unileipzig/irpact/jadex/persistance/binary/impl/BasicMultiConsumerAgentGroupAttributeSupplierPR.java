package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicMultiConsumerAgentGroupAttributeSupplier;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttributeSupplier;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicMultiConsumerAgentGroupAttributeSupplierPR extends BinaryPRBase<BasicMultiConsumerAgentGroupAttributeSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicMultiConsumerAgentGroupAttributeSupplierPR.class);

    public static final BasicMultiConsumerAgentGroupAttributeSupplierPR INSTANCE = new BasicMultiConsumerAgentGroupAttributeSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicMultiConsumerAgentGroupAttributeSupplier> getType() {
        return BasicMultiConsumerAgentGroupAttributeSupplier.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicMultiConsumerAgentGroupAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        for(Map.Entry<ConsumerAgentGroup, List<ConsumerAgentGroupAttributeSupplier>> entry: object.getMapping().entrySet()) {
            ConsumerAgentGroup cag = entry.getKey();
            manager.prepare(cag);

            List<ConsumerAgentGroupAttributeSupplier> suppliers = entry.getValue();
            for(ConsumerAgentGroupAttributeSupplier supplier: suppliers) {
                manager.prepare(supplier);
            }
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicMultiConsumerAgentGroupAttributeSupplier object, BinaryJsonData data, PersistManager manager) {
        Map<Long, List<Long>> ids = new LinkedHashMap<>();

        for(Map.Entry<ConsumerAgentGroup, List<ConsumerAgentGroupAttributeSupplier>> entry: object.getMapping().entrySet()) {
            ConsumerAgentGroup cag = entry.getKey();
            long cagId = manager.ensureGetUID(cag);
            List<Long> list = ids.computeIfAbsent(cagId, _cag -> new ArrayList<>());

            List<ConsumerAgentGroupAttributeSupplier> suppliers = entry.getValue();
            for(ConsumerAgentGroupAttributeSupplier supplier: suppliers) {
                long suppId = manager.ensureGetUID(supplier);
                list.add(suppId);
            }
        }

        data.putLongMultiLongMap(ids);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicMultiConsumerAgentGroupAttributeSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicMultiConsumerAgentGroupAttributeSupplier object = new BasicMultiConsumerAgentGroupAttributeSupplier();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicMultiConsumerAgentGroupAttributeSupplier object, RestoreManager manager) throws RestoreException {
        Map<Long, List<Long>> ids = data.getLongMultiLongMap();
        for(Map.Entry<Long, List<Long>> entry: ids.entrySet()) {
            long cagId = entry.getKey();
            ConsumerAgentGroup cag = manager.ensureGet(cagId);

            for(long suppId: entry.getValue()) {
                ConsumerAgentGroupAttributeSupplier supp = manager.ensureGet(suppId);
                object.add(cag, supp);
            }
        }
    }
}
