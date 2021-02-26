package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttributeSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributeSupplierPR implements Persister<BasicConsumerAgentGroupAttributeSupplier>, Restorer<BasicConsumerAgentGroupAttributeSupplier> {

    public static final BasicConsumerAgentGroupAttributeSupplierPR INSTANCE = new BasicConsumerAgentGroupAttributeSupplierPR();

    @Override
    public Class<BasicConsumerAgentGroupAttributeSupplier> getType() {
        return BasicConsumerAgentGroupAttributeSupplier.class;
    }

    @Override
    public Persistable persist(BasicConsumerAgentGroupAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getAttributeName());
        Map<Long, Long> map = new LinkedHashMap<>();
        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: object.getMapping().entrySet()) {
            map.put(
                    manager.ensureGetUID(entry.getKey()),
                    manager.ensureGetUID(entry.getValue())
            );
        }
        if(object.hasDefaultDisttribution()) {
            data.putLong(manager.ensureGetUID(object.getDefaultDisttribution()));
        } else {
            data.putNothing();
        }
        data.putLongLongMap(map);
        return data;
    }

    @Override
    public BasicConsumerAgentGroupAttributeSupplier initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicConsumerAgentGroupAttributeSupplier object = new BasicConsumerAgentGroupAttributeSupplier();
        object.setName(data.getText());
        object.setAttributeName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, BasicConsumerAgentGroupAttributeSupplier object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        Map<Long, Long> map = data.getLongLongMap();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(entry.getKey());
            UnivariateDoubleDistribution dist = manager.ensureGet(entry.getValue());
            object.put(cag, dist);
        }
        long id = data.getLong();
        if(id != BinaryJsonData.NOTHING_ID) {
            object.setDefaultDisttribution(manager.ensureGet(id));
        }
    }

    @Override
    public void finalize(Persistable persistable, BasicConsumerAgentGroupAttributeSupplier object, RestoreManager manager) {
    }
}
