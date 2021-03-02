package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentAttribute object = new BasicConsumerAgentAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentAttribute object, RestoreManager manager) {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
