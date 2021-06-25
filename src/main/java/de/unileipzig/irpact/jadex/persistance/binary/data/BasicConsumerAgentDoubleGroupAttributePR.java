package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentDoubleGroupAttributePR extends BinaryPRBase<BasicConsumerAgentDoubleGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentDoubleGroupAttributePR.class);

    public static final BasicConsumerAgentDoubleGroupAttributePR INSTANCE = new BasicConsumerAgentDoubleGroupAttributePR();

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
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentDoubleGroupAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getDistribution());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentDoubleGroupAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
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
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentDoubleGroupAttribute object, RestoreManager manager) throws RestoreException {
        object.setDistribution(manager.ensureGet(data.getLong()));
    }
}
