package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttributePR extends BinaryPRBase<BasicConsumerAgentAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentAttributePR.class);

    public static final BasicConsumerAgentAttributePR INSTANCE = new BasicConsumerAgentAttributePR();

    @Override
    public Class<BasicConsumerAgentAttribute> getType() {
        return BasicConsumerAgentAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Persistable initalizePersist(BasicConsumerAgentAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());

        data.putLong(manager.ensureGetUID(object.getGroup()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicConsumerAgentAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicConsumerAgentAttribute object = new BasicConsumerAgentAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicConsumerAgentAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
