package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(BasicProductGroup object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLongArray(manager.ensureGetAllUIDs(object.getGroupAttributes()));
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicProductGroup initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicProductGroup object = new BasicProductGroup();
        object.setName(data.getText());
        return object;
    }

    /*
     * Products werden ad hoch rekonstruiet
     */
    @Override
    public void setupRestore(Persistable persistable, BasicProductGroup object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.addAllGroupAttributes(manager.ensureGetAll(data.getLongArray(), ProductGroupAttribute[]::new));
    }
}
