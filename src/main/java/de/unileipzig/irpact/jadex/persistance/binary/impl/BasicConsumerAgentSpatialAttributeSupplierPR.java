package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentSpatialAttributeSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentSpatialAttributeSupplierPR extends BinaryPRBase<BasicConsumerAgentSpatialAttributeSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentSpatialAttributeSupplierPR.class);

    public static final BasicConsumerAgentSpatialAttributeSupplierPR INSTANCE = new BasicConsumerAgentSpatialAttributeSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicConsumerAgentSpatialAttributeSupplier> getType() {
        return BasicConsumerAgentSpatialAttributeSupplier.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentSpatialAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
            manager.prepare(entry.getKey());
            manager.prepare(entry.getValue());
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentSpatialAttributeSupplier object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Long> map = new LinkedHashMap<>();
        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
            map.put(
                    manager.ensureGetUID(entry.getKey()),
                    manager.ensureGetUID(entry.getValue())
            );
        }
        data.putLongLongMap(map);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentSpatialAttributeSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentSpatialAttributeSupplier object = new BasicConsumerAgentSpatialAttributeSupplier();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentSpatialAttributeSupplier object, RestoreManager manager) {
        Map<Long, Long> map = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
            UnivariateDoubleDistribution dist = manager.ensureGet(entry.getValue());
            object.put(cag, dist);
        }
    }
}
