package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributePR extends BinaryPRBase<BasicConsumerAgentGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributePR.class);

    public static final BasicConsumerAgentGroupAttributePR INSTANCE = new BasicConsumerAgentGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicConsumerAgentGroupAttribute> getType() {
        return BasicConsumerAgentGroupAttribute.class;
    }

    @Override
    public Persistable initalizePersist(BasicConsumerAgentGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getValue()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicConsumerAgentGroupAttribute initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicConsumerAgentGroupAttribute object = new BasicConsumerAgentGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicConsumerAgentGroupAttribute object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
