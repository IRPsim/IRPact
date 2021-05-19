package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAnnualAttributePR extends BinaryPRBase<BasicConsumerAgentAnnualAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentAnnualAttributePR.class);

    public static final BasicConsumerAgentAnnualAttributePR INSTANCE = new BasicConsumerAgentAnnualAttributePR();

    @Override
    public Class<BasicConsumerAgentAnnualAttribute> getType() {
        return BasicConsumerAgentAnnualAttribute.class;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicConsumerAgentAnnualAttribute object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getMapSupplier().getID());
        data.putText(object.getName());
        data.putBoolean(object.isArtificial());

        manager.prepareAll(object.getMapping().values());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicConsumerAgentAnnualAttribute object, BinaryJsonData data, PersistManager manager) throws PersistException {
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
    protected BasicConsumerAgentAnnualAttribute doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        MapSupplier mapSupplier = MapSupplier.getValid(data.getInt());
        BasicConsumerAgentAnnualAttribute object = new BasicConsumerAgentAnnualAttribute(mapSupplier);
        object.setName(data.getText());
        object.setArtificial(data.getBoolean());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicConsumerAgentAnnualAttribute object, RestoreManager manager) {
        Map<Long, Long> idMap = data.getLongLongMap();
        BinaryJsonData.mapFromLongLongMap(
                idMap,
                BinaryJsonData.LONG2INT,
                BinaryJsonData.ensureGet(manager),
                object.getMapping()
        );
    }
}
