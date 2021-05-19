package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentProductRelatedAttributePR extends BinaryPRBase<BasicConsumerAgentProductRelatedAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentProductRelatedAttributePR.class);

    public static final BasicConsumerAgentProductRelatedAttributePR INSTANCE = new BasicConsumerAgentProductRelatedAttributePR();

    @Override
    public Class<BasicConsumerAgentProductRelatedAttribute> getType() {
        return BasicConsumerAgentProductRelatedAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentProductRelatedAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepare(object.getGroup());
        manager.prepareAll(object.getMapping());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentProductRelatedAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGroup()));

        Map<Long, Long> idMap = BinaryJsonData.mapToLongLongMap(object.getMapping(), manager);
        data.putLongLongMap(idMap);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentProductRelatedAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicConsumerAgentProductRelatedAttribute object = new BasicConsumerAgentProductRelatedAttribute();
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentProductRelatedAttribute object, RestoreManager manager) throws RestoreException {
        object.setGroup(manager.ensureGet(data.getLong()));

        Map<Long, Long> idMap = data.getLongLongMap();
        BinaryJsonData.mapFromLongLongMap(idMap, manager, object.getMapping());
    }
}
