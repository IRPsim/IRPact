package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributePR implements Persister<BasicConsumerAgentGroupAttribute>, Restorer<BasicConsumerAgentGroupAttribute> {

    public static final BasicConsumerAgentGroupAttributePR INSTANCE = new BasicConsumerAgentGroupAttributePR();

    @Override
    public Class<BasicConsumerAgentGroupAttribute> getType() {
        return BasicConsumerAgentGroupAttribute.class;
    }

    @Override
    public Persistable persist(BasicConsumerAgentGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getValue()));
        return data;
    }

    @Override
    public BasicConsumerAgentGroupAttribute initalize(Persistable persistable) {
        return new BasicConsumerAgentGroupAttribute();
    }

    @Override
    public void setup(Persistable persistable, BasicConsumerAgentGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setName(data.getText());
        object.setDistribution(manager.ensureGet(data.getLong()));
    }

    @Override
    public void finalize(Persistable persistable, BasicConsumerAgentGroupAttribute object, RestoreManager manager) {
    }
}
