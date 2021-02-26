package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

/**
 * @author Daniel Abitz
 */
public class CompleteGraphTopologyPR implements Persister<CompleteGraphTopology>, Restorer<CompleteGraphTopology> {

    public static final CompleteGraphTopologyPR INSTANCE = new CompleteGraphTopologyPR();

    @Override
    public Class<CompleteGraphTopology> getType() {
        return CompleteGraphTopology.class;
    }

    @Override
    public Persistable persist(CompleteGraphTopology object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putInt(object.getEdgeType().id());
        data.putDouble(object.getInitialWeight());
        return data;
    }

    @Override
    public CompleteGraphTopology initalize(Persistable persistable, RestoreManager manager) {
        return new CompleteGraphTopology();
    }

    @Override
    public void setup(Persistable persistable, CompleteGraphTopology object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        object.setInitialWeight(data.getDouble());
    }

    @Override
    public void finalize(Persistable persistable, CompleteGraphTopology object, RestoreManager manager) {
    }
}
