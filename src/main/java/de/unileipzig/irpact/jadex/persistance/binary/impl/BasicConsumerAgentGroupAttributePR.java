package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentGroupValueAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributePR extends BinaryPRBase<BasicConsumerAgentGroupValueAttribute> {

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
    public Class<BasicConsumerAgentGroupValueAttribute> getType() {
        return BasicConsumerAgentGroupValueAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentGroupValueAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getUnivariateDoubleDistributionValue());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentGroupValueAttribute object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getValue()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentGroupValueAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentGroupValueAttribute object = new BasicConsumerAgentGroupValueAttribute();
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentGroupValueAttribute object, RestoreManager manager) {
        object.setUnivariateDoubleDistributionValue(manager.ensureGet(data.getLong()));
    }
}
