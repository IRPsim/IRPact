package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttributePR implements Persister<BasicConsumerAgentAttribute>, Restorer<BasicConsumerAgentAttribute> {

    public static final BasicConsumerAgentAttributePR INSTANCE = new BasicConsumerAgentAttributePR();

    @Override
    public Class<BasicConsumerAgentAttribute> getType() {
        return BasicConsumerAgentAttribute.class;
    }

    @Override
    public Persistable persist(BasicConsumerAgentAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());

        data.putLong(manager.ensureGetUID(object.getGroup()));
        return data;
    }

    @Override
    public BasicConsumerAgentAttribute initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicConsumerAgentAttribute object = new BasicConsumerAgentAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    public void setup(Persistable persistable, BasicConsumerAgentAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicConsumerAgentAttribute object, RestoreManager manager) {
    }
}
