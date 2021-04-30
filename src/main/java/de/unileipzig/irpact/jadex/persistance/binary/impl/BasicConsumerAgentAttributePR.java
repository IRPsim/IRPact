package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttributePR extends BinaryPRBase<ConsumerAgentDoubleAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentAttributePR.class);

    public static final BasicConsumerAgentAttributePR INSTANCE = new BasicConsumerAgentAttributePR();

    @Override
    public Class<ConsumerAgentDoubleAttribute> getType() {
        return ConsumerAgentDoubleAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ConsumerAgentDoubleAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(ConsumerAgentDoubleAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ConsumerAgentDoubleAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        ConsumerAgentDoubleAttribute object = new ConsumerAgentDoubleAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        object.setArtificial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ConsumerAgentDoubleAttribute object, RestoreManager manager) {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
