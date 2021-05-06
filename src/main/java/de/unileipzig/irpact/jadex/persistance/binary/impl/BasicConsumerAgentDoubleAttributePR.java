package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentDoubleAttributePR extends BinaryPRBase<BasicConsumerAgentDoubleAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentDoubleAttributePR.class);

    public static final BasicConsumerAgentDoubleAttributePR INSTANCE = new BasicConsumerAgentDoubleAttributePR();

    @Override
    public Class<BasicConsumerAgentDoubleAttribute> getType() {
        return BasicConsumerAgentDoubleAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentDoubleAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getDoubleValue());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getGroup());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentDoubleAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGroup()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentDoubleAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentDoubleAttribute object = new BasicConsumerAgentDoubleAttribute();
        object.setName(data.getText());
        object.setDoubleValue(data.getDouble());
        object.setArtificial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentDoubleAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));
    }
}
