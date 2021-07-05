package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SupportedGraphStructure;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraphPR extends BinaryPRBase<BasicSocialGraph> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSocialGraphPR.class);

    public static final BasicSocialGraphPR INSTANCE = new BasicSocialGraphPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicSocialGraph> getType() {
        return BasicSocialGraph.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicSocialGraph object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        for(SocialGraph.Type type: SocialGraph.Type.values()) {
            for(SocialGraph.Edge edge: object.getEdges(type)) {
                manager.prepare(edge);
            }
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicSocialGraph object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putInt(object.getStructure().getID());

        //Knoten werden ueber Agenten rekonstruiert
        //trick: wir speichern keine Kantenliste, sondern rekonstruieren diese auch ad hoc
        int totalEdgeCount = 0;
        for(SocialGraph.Type type: SocialGraph.Type.values()) {
            int typeEdgeCount = 0;
            for(SocialGraph.Edge edge: object.getEdges(type)) {
                manager.ensureGetUID(edge);
                typeEdgeCount++;
                totalEdgeCount++;
            }
            log().trace("stores {} edges (type = {})", typeEdgeCount, type);
        }
        log().trace("total edges stored: {}", totalEdgeCount);

        object.logChecksums();
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicSocialGraph doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        int id = data.getInt();
        SupportedGraphStructure structure = SupportedGraphStructure.get(id);
        if(structure.isUnknown()) {
            throw new RestoreException("unknown graph structure: " + id);
        }
        return new BasicSocialGraph(structure);
    }
}
