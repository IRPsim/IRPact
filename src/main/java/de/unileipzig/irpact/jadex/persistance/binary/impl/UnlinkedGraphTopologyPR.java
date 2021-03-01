package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopologyPR extends BinaryPRBase<UnlinkedGraphTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnlinkedGraphTopologyPR.class);

    public static final UnlinkedGraphTopologyPR INSTANCE = new UnlinkedGraphTopologyPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UnlinkedGraphTopology> getType() {
        return UnlinkedGraphTopology.class;
    }

    @Override
    public Persistable initalizePersist(UnlinkedGraphTopology object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getEdgeType().id());
        storeHash(object, data);
        return data;
    }

    @Override
    public UnlinkedGraphTopology initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        UnlinkedGraphTopology object = new UnlinkedGraphTopology();
        object.setName(data.getText());
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, UnlinkedGraphTopology object, RestoreManager manager) {
    }
}
