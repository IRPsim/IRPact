package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAnnualGroupAttributePR extends BinaryPRBase<BasicConsumerAgentAnnualGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentAnnualGroupAttributePR.class);

    public static final BasicConsumerAgentAnnualGroupAttributePR INSTANCE = new BasicConsumerAgentAnnualGroupAttributePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicConsumerAgentAnnualGroupAttribute> getType() {
        return BasicConsumerAgentAnnualGroupAttribute.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentAnnualGroupAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getMapSupplier().getID());
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepareAll(object.getMapping().values());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentAnnualGroupAttribute object, BinaryJsonData data, PersistManager manager) {
        Map<Long, Long> idMap = BinaryJsonData.mapToLongLongMap(
                object.getMapping(),
                BinaryJsonData.INT2LONG,
                BinaryJsonData.ensureGetUID(manager)
        );
        data.putLongLongMap(idMap);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicConsumerAgentAnnualGroupAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        MapSupplier mapSupplier = MapSupplier.getValid(data.getInt());
        BasicConsumerAgentAnnualGroupAttribute object = new BasicConsumerAgentAnnualGroupAttribute(mapSupplier);
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentAnnualGroupAttribute object, RestoreManager manager) {
        Map<Long, Long> idMap = data.getLongLongMap();
        BinaryJsonData.mapFromLongLongMap(
                idMap,
                BinaryJsonData.LONG2INT,
                BinaryJsonData.ensureGet(manager),
                object.getMapping()
        );
    }
}
