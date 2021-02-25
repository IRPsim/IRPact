package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentSpatialAttributeSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentSpatialAttributeSupplierPR implements Persister<BasicConsumerAgentSpatialAttributeSupplier>, Restorer<BasicConsumerAgentSpatialAttributeSupplier> {

    public static final BasicConsumerAgentSpatialAttributeSupplierPR INSTANCE = new BasicConsumerAgentSpatialAttributeSupplierPR();

    @Override
    public Class<BasicConsumerAgentSpatialAttributeSupplier> getType() {
        return BasicConsumerAgentSpatialAttributeSupplier.class;
    }

    @Override
    public Persistable persist(BasicConsumerAgentSpatialAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        Map<Long, Long> map = new LinkedHashMap<>();
        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
            map.put(
                    manager.ensureGetUID(entry.getKey()),
                    manager.ensureGetUID(entry.getValue())
            );
        }
        data.putLongLongMap(map);
        return data;
    }

    @Override
    public BasicConsumerAgentSpatialAttributeSupplier initalize(Persistable persistable) {
        return new BasicConsumerAgentSpatialAttributeSupplier();
    }

    @Override
    public void setup(Persistable persistable, BasicConsumerAgentSpatialAttributeSupplier object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        Map<Long, Long> map = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
            UnivariateDoubleDistribution dist = manager.ensureGet(entry.getValue());
            object.put(cag, dist);
        }
    }

    @Override
    public void finalize(Persistable persistable, BasicConsumerAgentSpatialAttributeSupplier object, RestoreManager manager) {
    }
}
