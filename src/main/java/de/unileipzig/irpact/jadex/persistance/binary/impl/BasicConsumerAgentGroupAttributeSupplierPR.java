package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentGroupAttributeSupplier;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@Todo("hier fuer custom supplier nutzen")
public class BasicConsumerAgentGroupAttributeSupplierPR extends BinaryPRBase<BasicConsumerAgentGroupAttributeSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributeSupplierPR.class);

    public static final BasicConsumerAgentGroupAttributeSupplierPR INSTANCE = new BasicConsumerAgentGroupAttributeSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicConsumerAgentGroupAttributeSupplier> getType() {
        return BasicConsumerAgentGroupAttributeSupplier.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentGroupAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getAttributeName());

//        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
//            manager.prepare(entry.getKey());
//            manager.prepare(entry.getValue());
//        }
//        if(object.hasDefaultDisttribution()) {
//            manager.prepare(object.getDefaultDisttribution());
//        }
        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentGroupAttributeSupplier object, BinaryJsonData data, PersistManager manager) {
//        Map<Long, Long> map = new LinkedHashMap<>();
//        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
//            map.put(
//                    manager.ensureGetUID(entry.getKey()),
//                    manager.ensureGetUID(entry.getValue())
//            );
//        }
//        data.putLongLongMap(map);
//
//        if(object.hasDefaultDisttribution()) {
//            data.putLong(manager.ensureGetUID(object.getDefaultDisttribution()));
//        } else {
//            data.putNothing();
//        }
        data.putLong(manager.ensureGetUID(object.getDistribution()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentGroupAttributeSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentGroupAttributeSupplier object = new BasicConsumerAgentGroupAttributeSupplier();
        object.setName(data.getText());
        object.setAttributeName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentGroupAttributeSupplier object, RestoreManager manager) {
//        Map<Long, Long> map = data.getLongLongMap();
//        for(Map.Entry<Long, Long> entry: map.entrySet()) {
//            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
//            UnivariateDoubleDistribution dist = manager.ensureGet(entry.getValue());
//            object.put(cag, dist);
//        }

        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
