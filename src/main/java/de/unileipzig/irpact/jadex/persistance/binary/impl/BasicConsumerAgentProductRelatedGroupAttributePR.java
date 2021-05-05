package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentProductRelatedGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentProductRelatedGroupAttributePR extends BinaryPRBase<BasicConsumerAgentProductRelatedGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentProductRelatedGroupAttributePR.class);

    public static final BasicConsumerAgentProductRelatedGroupAttributePR INSTANCE = new BasicConsumerAgentProductRelatedGroupAttributePR();

    @Override
    public Class<BasicConsumerAgentProductRelatedGroupAttribute> getType() {
        return BasicConsumerAgentProductRelatedGroupAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentProductRelatedGroupAttribute object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getMapSupplier().getID());
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepareAll(object.getMapping());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentProductRelatedGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Long> idMap = BinaryJsonData.mapToLongLongMap(object.getMapping(), manager);
        data.putLongLongMap(idMap);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentProductRelatedGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        MapSupplier mapSupplier = MapSupplier.getValid(data.getInt());
        BasicConsumerAgentProductRelatedGroupAttribute object = new BasicConsumerAgentProductRelatedGroupAttribute(mapSupplier);
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentProductRelatedGroupAttribute object, RestoreManager manager) {
        Map<Long, Long> idMap = data.getLongLongMap();
        BinaryJsonData.mapFromLongLongMap(
                idMap,
                manager,
                object.getMapping()
        );
    }
}
