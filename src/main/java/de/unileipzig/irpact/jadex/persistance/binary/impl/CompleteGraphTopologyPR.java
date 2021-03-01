package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class CompleteGraphTopologyPR extends BinaryPRBase<CompleteGraphTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CompleteGraphTopologyPR.class);

    public static final CompleteGraphTopologyPR INSTANCE = new CompleteGraphTopologyPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<CompleteGraphTopology> getType() {
        return CompleteGraphTopology.class;
    }

    @Override
    public Persistable initalizePersist(CompleteGraphTopology object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putInt(object.getEdgeType().id());
        data.putDouble(object.getInitialWeight());
        storeHash(object, data);
        return data;
    }

    @Override
    public CompleteGraphTopology initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new CompleteGraphTopology();
    }

    @Override
    public void setupRestore(Persistable persistable, CompleteGraphTopology object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        object.setInitialWeight(data.getDouble());
    }
}
