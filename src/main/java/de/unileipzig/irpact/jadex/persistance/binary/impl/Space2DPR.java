package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class Space2DPR extends BinaryPRBase<Space2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Space2DPR.class);

    public static final Space2DPR INSTANCE = new Space2DPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<Space2D> getType() {
        return Space2D.class;
    }

    @Override
    public Persistable initalizePersist(Space2D object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getMetric().id());
        storeHash(object, data);
        return data;
    }

    @Override
    public Space2D initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        Space2D object = new Space2D();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, Space2D object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));

        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setMetric(Metric2D.get(data.getInt()));
    }
}
