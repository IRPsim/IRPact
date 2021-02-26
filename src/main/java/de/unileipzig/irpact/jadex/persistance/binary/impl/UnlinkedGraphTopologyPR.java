package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopologyPR implements Persister<UnlinkedGraphTopology>, Restorer<UnlinkedGraphTopology> {

    public static final UnlinkedGraphTopologyPR INSTANCE = new UnlinkedGraphTopologyPR();

    @Override
    public Class<UnlinkedGraphTopology> getType() {
        return UnlinkedGraphTopology.class;
    }

    @Override
    public Persistable persist(UnlinkedGraphTopology object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putInt(object.getEdgeType().id());
        return data;
    }

    @Override
    public UnlinkedGraphTopology initalize(Persistable persistable, RestoreManager manager) {
        return new UnlinkedGraphTopology();
    }

    @Override
    public void setup(Persistable persistable, UnlinkedGraphTopology object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
    }

    @Override
    public void finalize(Persistable persistable, UnlinkedGraphTopology object, RestoreManager manager) {
    }
}
