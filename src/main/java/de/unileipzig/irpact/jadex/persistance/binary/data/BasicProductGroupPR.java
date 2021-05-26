package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.attribute.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupPR extends BinaryPRBase<BasicProductGroup> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicProductGroupPR.class);

    public static final BasicProductGroupPR INSTANCE = new BasicProductGroupPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicProductGroup> getType() {
        return BasicProductGroup.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicProductGroup object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepareAll(object.getGroupAttributes());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicProductGroup object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLongArray(manager.ensureGetAllUIDs(object.getGroupAttributes()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicProductGroup doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicProductGroup object = new BasicProductGroup();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicProductGroup object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ProductGroupAttribute[]::new));
    }
}
