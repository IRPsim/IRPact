package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicConsumerAgentGroupAttribute> getType() {
        return BasicConsumerAgentGroupAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getValue());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getValue()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentGroupAttribute object = new BasicConsumerAgentGroupAttribute();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentGroupAttribute object, RestoreManager manager) {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
