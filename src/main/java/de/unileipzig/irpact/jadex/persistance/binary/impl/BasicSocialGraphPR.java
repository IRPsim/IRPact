package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SupportedGraphStructure;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
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

    @Override
    public Persistable initalizePersist(BasicSocialGraph object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putInt(object.getStructure().getID());
        storeHash(object, data);

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

        return data;
    }

    @Override
    public BasicSocialGraph initalizeRestore(Persistable persistable, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        int graphStructureId = data.getInt();
        SupportedGraphStructure structure = SupportedGraphStructure.get(graphStructureId);
        if(structure == SupportedGraphStructure.UNKNOWN) {
            throw new RestoreException("Unsupported structure");
        }
        return new BasicSocialGraph(structure);
    }

    //Alles in Edge ausgelagert + Agent
    @Override
    public void setupRestore(Persistable persistable, BasicSocialGraph object, RestoreManager manager) {
    }
}
