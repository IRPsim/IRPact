package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.v2.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributePR extends BinaryPRBase<BasicConsumerAgentDoubleGroupAttribute> {

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
    public Class<BasicConsumerAgentDoubleGroupAttribute> getType() {
        return BasicConsumerAgentDoubleGroupAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentDoubleGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentDoubleGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getDistribution()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentDoubleGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentDoubleGroupAttribute object = new BasicConsumerAgentDoubleGroupAttribute();
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentDoubleGroupAttribute object, RestoreManager manager) {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
