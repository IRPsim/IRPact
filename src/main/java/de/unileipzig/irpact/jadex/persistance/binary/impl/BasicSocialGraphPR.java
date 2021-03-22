package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SupportedGraphStructure;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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
    protected BinaryJsonData doInitalizePersist(BasicSocialGraph object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        for(SocialGraph.Type type: SocialGraph.Type.values()) {
            for(SocialGraph.Edge edge: object.getEdges(type)) {
                manager.prepare(edge);
            }
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicSocialGraph object, BinaryJsonData data, PersistManager manager) {
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
            log().debug("stores {} edges (type = {})", typeEdgeCount, type);
        }
        log().debug("total edges stored: {}", totalEdgeCount);
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicSocialGraph doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SupportedGraphStructure structure = SupportedGraphStructure.get(data.getInt());
        return new BasicSocialGraph(structure);
    }
}