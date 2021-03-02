package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UnlinkedGraphTopology object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getEdgeType().id());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UnlinkedGraphTopology doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UnlinkedGraphTopology object = new UnlinkedGraphTopology();
        object.setName(data.getText());
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        return object;
    }
}
