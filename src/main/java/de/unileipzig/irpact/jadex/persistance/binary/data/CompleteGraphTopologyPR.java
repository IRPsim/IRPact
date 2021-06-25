package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(CompleteGraphTopology object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getEdgeType().id());
        data.putDouble(object.getInitialWeight());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected CompleteGraphTopology doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        CompleteGraphTopology object = new CompleteGraphTopology();
        object.setName(data.getText());
        object.setEdgeType(SocialGraph.Type.get(data.getInt()));
        object.setInitialWeight(data.getDouble());
        return object;
    }
}
